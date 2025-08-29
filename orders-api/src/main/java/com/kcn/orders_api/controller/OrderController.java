package com.kcn.orders_api.controller;

import com.kcn.orders_api.dto.request.OrderRequest;
import com.kcn.orders_api.dto.response.OrderResponse;
import com.kcn.orders_api.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order REST API Endpoints", description = "Operations related to orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Create a new order", description = "Creates a new order for the current user", security = @SecurityRequirement(name ="bearerAuth"))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
        OrderResponse response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get order by ID", description = "Returns a single order by its ID", security = @SecurityRequirement(name ="bearerAuth"))
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable @Min(1) Long orderId) {
        OrderResponse response = orderService.getOrder(orderId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get orders for current user", description = "Returns all orders for the authenticated user", security = @SecurityRequirement(name ="bearerAuth"))
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrdersForCurrentUser() {
        List<OrderResponse> responses = orderService.getOrdersForCurrentUser();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Mark order as delivered", description = "Allows an admin to update the status of an order to DELIVERED", security = @SecurityRequirement(name ="bearerAuth"))
    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable @Min(1) Long orderId) {
        OrderResponse updatedOrder = orderService.updateOrderStatus(orderId);
        return ResponseEntity.ok(updatedOrder);
    }
}
