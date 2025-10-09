package com.kcn.e_shop.exception;

import java.util.UUID;

public class ProductDeletionException extends RuntimeException {
    private final UUID productId;

    public ProductDeletionException(String message, UUID productId) {
        super(message);
        this.productId = productId;
    }

    public UUID getProductId() {
        return productId;
    }
}
