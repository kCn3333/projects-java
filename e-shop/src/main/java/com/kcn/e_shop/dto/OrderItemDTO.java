package com.kcn.e_shop.dto;

import java.math.BigDecimal;

public record OrderItemDTO(
        String productName,
        BigDecimal productPrice,
        int quantity,
        double subtotal
) {}