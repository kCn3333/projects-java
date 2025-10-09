package com.kcn.e_shop.mapper;

import com.kcn.e_shop.dto.ProductDTO;
import com.kcn.e_shop.entity.Category;
import com.kcn.e_shop.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDTO toDTO(Product product) {
        if (product == null) {
            return null;
        }
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .brand(product.getBrand())
                .description(product.getDescription())
                .features(product.getFeatures())
                .price(product.getPrice())
                .imagePath(product.getImagePath())
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .stock(product.getStock())
                .build();
    }

    public Product toEntity(ProductDTO productDTO, Category category) {
        if (productDTO  == null) {
            return null;
        }
        return Product.builder()
                .id(productDTO.getId())
                .name(productDTO.getName())
                .brand(productDTO.getBrand())
                .description(productDTO.getDescription())
                .features(productDTO.getFeatures())
                .price(productDTO.getPrice())
                .imagePath(productDTO.getImagePath())
                .category(category)
                .createdAt(productDTO.getCreatedAt())
                .updatedAt(productDTO.getUpdatedAt())
                .stock(productDTO.getStock())
                .build();
    }

    public void updateEntityFromDTO(ProductDTO productDTO, Product product, Category category) {
        if (productDTO == null || product == null) {
            return;
        }

        product.setName(productDTO.getName());
        product.setBrand(productDTO.getBrand());
        product.setDescription(productDTO.getDescription());
        product.setFeatures(productDTO.getFeatures());
        product.setPrice(productDTO.getPrice());
        product.setImagePath(productDTO.getImagePath());
        product.setCategory(category);
        product.setUpdatedAt(productDTO.getUpdatedAt());
        product.setCreatedAt(productDTO.getCreatedAt());
        product.setStock(productDTO.getStock());
    }
}
