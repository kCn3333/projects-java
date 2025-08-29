package com.kcn.orders_api.repository;

import com.kcn.orders_api.model.Order;
import com.kcn.orders_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);

    Optional<Order> findWithItemsById(Long orderId);

    List<Order> findByUserOrderByCreatedAtDesc(User currentUser);

    // w razie potrzeby -> pobranie zamówienia z lazy items
    //@EntityGraph(attributePaths = "items")
    //Optional<Order> findWithItemsById(Long id);
}
