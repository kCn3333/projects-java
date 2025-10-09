package com.kcn.e_shop.mapper;

import com.kcn.e_shop.dto.OrderDTO;
import com.kcn.e_shop.dto.OrderItemDTO;
import com.kcn.e_shop.entity.Order;
import com.kcn.e_shop.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderDTO toDTO(Order order) {
        if (order == null) {
            return null;
        }

        List<OrderItemDTO> itemDTOs = order.getItems().stream()
                .map(this::toItemDTO)
                .collect(Collectors.toList());

        double total = itemDTOs.stream()
                .mapToDouble(OrderItemDTO::subtotal)
                .sum();

        return new OrderDTO(
                order.getId(),
                order.getCreatedAt(),
                order.getUser().getUsername(),
                order.getUser().getEmail(),
                itemDTOs,
                order.getStatus(),
                total
        );
    }

    private OrderItemDTO toItemDTO(OrderItem item) {
        if (item == null) {
            return null;
        }

        double subtotal = item.getProduct().getPrice().doubleValue() * item.getQuantity();

        return new OrderItemDTO(
                item.getProduct().getName(),
                item.getProduct().getPrice(),
                item.getQuantity(),
                subtotal
        );
    }

    public List<OrderDTO> toDTOList(List<Order> orders) {
        return orders.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}