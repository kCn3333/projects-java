package com.kcn.e_shop.exception;

public class CategoryDeletionException extends RuntimeException {
    private final String categoryName;

    public CategoryDeletionException(String message, String categoryName) {
        super(message);
        this.categoryName=categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }
}