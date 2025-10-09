package com.kcn.e_shop.dto;

import com.kcn.e_shop.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItem {
    private Product product;
    private int quantity;
}
