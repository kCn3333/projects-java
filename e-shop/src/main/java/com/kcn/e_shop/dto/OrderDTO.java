package com.kcn.e_shop.dto;

import com.kcn.e_shop.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDTO(
        Long id,
        LocalDateTime createdAt,
        String userName,
        String userEmail,
        List<OrderItemDTO> items,
        OrderStatus status,
        double total
) {}