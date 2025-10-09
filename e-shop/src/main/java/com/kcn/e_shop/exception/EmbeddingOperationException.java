package com.kcn.e_shop.exception;

public class EmbeddingOperationException extends RuntimeException {
    public EmbeddingOperationException(String message) {
        super(message);
    }

    public EmbeddingOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
