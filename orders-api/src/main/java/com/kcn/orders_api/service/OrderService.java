package com.kcn.orders_api.service;

import com.kcn.orders_api.dto.request.OrderRequest;
import com.kcn.orders_api.dto.response.OrderResponse;

import java.util.List;


public interface OrderService {
    OrderResponse createOrder(OrderRequest request);

    OrderResponse getOrder(Long orderId);
    List<OrderResponse> getOrdersForCurrentUser();
    OrderResponse updateOrderStatus(Long orderId);
}
