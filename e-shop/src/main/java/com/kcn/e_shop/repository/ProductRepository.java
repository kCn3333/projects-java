package com.kcn.e_shop.repository;

import com.kcn.e_shop.entity.Category;
import com.kcn.e_shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository <Product, UUID> {
    Iterable<Object> findByCategory(Category category);

    void deleteAllByCategory(Category category);

    //
    @Query("SELECT p FROM Product p WHERE p.id IN :ids ORDER BY FUNCTION('FIELD', p.id, :ids)")
    List<Product> findAllByIdInOrder(@Param("ids") List<UUID> ids);

    @Query("SELECT p FROM Product p JOIN FETCH p.category")
    List<Product> findAllWithCategory();


}
