package com.kcn.orders_api.repository;

import com.kcn.orders_api.model.Order;
import com.kcn.orders_api.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder(Order order);
}
