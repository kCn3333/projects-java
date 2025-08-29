package com.kcn.orders_api.service;

import com.kcn.orders_api.dto.request.OrderRequest;
import com.kcn.orders_api.dto.response.OrderResponse;
import com.kcn.orders_api.model.Order;
import com.kcn.orders_api.model.OrderItem;
import com.kcn.orders_api.model.Product;
import com.kcn.orders_api.model.User;
import com.kcn.orders_api.repository.OrderRepository;
import com.kcn.orders_api.repository.ProductRepository;
import com.kcn.orders_api.util.AuthenticationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final AuthenticationUtils authenticationUtils;

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository, AuthenticationUtils authenticationUtils) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.authenticationUtils = authenticationUtils;
    }

    @Transactional
    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {
        User currentUser = authenticationUtils.getCurrentUser();

        Order order = Order.builder()
                .user(currentUser)
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        double totalAmount = 0.0;
        List<OrderItem> items = new ArrayList<>();

        for (OrderRequest.OrderItemRequest itemRequest : orderRequest.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Product with id " + itemRequest.getProductId() + " not found"));

            // Sprawdzenie dostępności
            if (itemRequest.getQuantity() > product.getQuantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Insufficient stock for product: " + product.getName());
            }

            double unitPrice = product.getPrice();
            double totalPrice = unitPrice * itemRequest.getQuantity();

            // Tworzenie pozycji zamówienia
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(unitPrice)
                    .totalPrice(totalPrice)
                    .build();

            items.add(orderItem);
            totalAmount += totalPrice;

            // Zmniejszenie stanu magazynowego
            product.setQuantity(product.getQuantity() - itemRequest.getQuantity());
            product.setUpdatedAt(LocalDateTime.now());
            productRepository.save(product);
        }

        order.setItems(items);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        return mapToOrderResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    @Override
    public OrderResponse getOrder(Long orderId) {
        Order order = orderRepository.findWithItemsById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Order with id " + orderId + " not found"));
        return mapToOrderResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersForCurrentUser() {
        User currentUser = authenticationUtils.getCurrentUser();

        List<Order> orders = orderRepository.findByUserOrderByCreatedAtDesc(currentUser);

        return orders.stream()
                .map(this::mapToOrderResponse)
                .toList();
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        order.setStatus("DELIVERED");
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        return mapToOrderResponse(order);
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .userEmail(order.getUser().getEmail())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .items(order.getItems().stream()
                        .map(item -> OrderResponse.OrderItemResponse.builder()
                                .productId(item.getProduct().getId())
                                .productName(item.getProduct().getName())
                                .quantity(item.getQuantity())
                                .price(item.getUnitPrice())
                                .subtotal(item.getTotalPrice())
                                .build())
                        .toList())
                .build();
    }

}
