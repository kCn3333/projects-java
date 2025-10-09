package com.kcn.e_shop.repository;

import com.kcn.e_shop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

   Optional<User> findByUsername(String username);
}
