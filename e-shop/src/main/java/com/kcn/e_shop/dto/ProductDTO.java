package com.kcn.e_shop.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private UUID id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Features are required")
    private String features;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be positive")
    private BigDecimal price;

    private String imagePath;

    @NotBlank(message = "Category is required")
    private String categoryName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stock;

    public static ProductDTO emptyWithCategory(String categoryName) {
        return ProductDTO.builder()
                .categoryName(categoryName)
                .createdAt(LocalDateTime.now())
                .stock(0)
                .price(BigDecimal.ZERO)
                .build();
    }
    public ProductDTO withImagePath(String newImagePath) {
        return new ProductDTO(
                this.id,
                this.name,
                this.brand,
                this.description,
                this.features,
                this.price,
                newImagePath,       //
                this.categoryName,
                this.createdAt,
                LocalDateTime.now(),
                this.stock
        );
    }

    public boolean isNew() {
        return this.id == null;
    }
}