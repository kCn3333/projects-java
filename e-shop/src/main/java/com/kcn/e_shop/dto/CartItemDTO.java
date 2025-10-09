package com.kcn.e_shop.dto;

public record CartItemDTO(
        ProductDTO product,
        int quantity
) {}
