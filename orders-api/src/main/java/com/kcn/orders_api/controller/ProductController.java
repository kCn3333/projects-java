package com.kcn.orders_api.controller;

import com.kcn.orders_api.dto.request.ProductRequest;
import com.kcn.orders_api.dto.response.ProductResponse;
import com.kcn.orders_api.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products REST API", description = "CRUD operations for products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Create a product", security = @SecurityRequirement(name ="bearerAuth"))
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.createProduct(request));
    }

    @Operation(summary = "Get a product by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable @Min(1) Long id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @Operation(summary = "Get all products")
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @Operation(summary = "Update a product", security = @SecurityRequirement(name ="bearerAuth"))
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable @Min(1) Long id,
                                                         @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @Operation(summary = "Delete a product", security = @SecurityRequirement(name ="bearerAuth"))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable @Min(1) Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
