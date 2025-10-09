package com.kcn.e_shop.repository;

import com.kcn.e_shop.entity.Order;
import com.kcn.e_shop.entity.OrderItem;
import com.kcn.e_shop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByCreatedAtDesc(User user);
    List<Order> findAllByOrderByCreatedAtDesc();
}

