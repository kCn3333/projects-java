package com.kcn.e_shop.service;

import com.kcn.e_shop.dto.CartItem;
import com.kcn.e_shop.dto.OrderDTO;
import com.kcn.e_shop.entity.*;
import com.kcn.e_shop.mapper.OrderMapper;
import com.kcn.e_shop.repository.OrderItemRepository;
import com.kcn.e_shop.repository.OrderRepository;
import com.kcn.e_shop.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final UserService userService;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderDTO createOrderFromCart(HttpSession session, Long userId) {
        User user = userService.getUserEntityById(userId);

        List<CartItem> cartItems = cartService.getCartItems(session);

        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        // Create order
        Order order = Order.builder()
                .user(user)
                .createdAt(LocalDateTime.now())
                .status(OrderStatus.RECEIVED)
                .items(new ArrayList<>())
                .build();

        Order savedOrder = orderRepository.save(order);

        // Create order items and update product stock
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            // Check stock
            if (product.getStock() < cartItem.getQuantity()) {
                throw new IllegalStateException("Not enough stock for product: " + product.getName());
            }

            // Update product stock
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            // Create order item
            OrderItem orderItem = OrderItem.builder()
                    .order(savedOrder)
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .build();

            OrderItem savedOrderItem = orderItemRepository.save(orderItem);
            savedOrder.getItems().add(savedOrderItem);
        }

        // Clear cart after successful order
        cartService.clearCart(session);
        log.info("[Order] "+ user.getUsername() + " placed an order, id: "+order.getId());
        return orderMapper.toDTO(savedOrder);
    }

    public List<OrderDTO> getUserOrders(Long userId) {
        User user = userService.getUserEntityById(userId);
        List<Order> orders = orderRepository.findByUserOrderByCreatedAtDesc(user);
        return orderMapper.toDTOList(orders);
    }

    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAllByOrderByCreatedAtDesc();
        return orderMapper.toDTOList(orders);
    }

    public OrderDTO getOrderById(Long id, Long userId) {
        return orderRepository.findById(id)
                .filter(order -> order.getUser().getId().equals(userId))
                .map(orderMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + id) );
    }

    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.setStatus(status);
        orderRepository.save(order);
        log.info("[Order] id: "+order.getId()+", status changed to "+status.name());
    }
}