package com.kcn.e_shop.repository;

import com.kcn.e_shop.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository <Category, Long> {
    Optional<Category> findByName(String s);

}
