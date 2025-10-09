package com.kcn.e_shop.service;

import com.kcn.e_shop.entity.Category;
import com.kcn.e_shop.exception.CategoryDeletionException;
import com.kcn.e_shop.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public String getCategoryNameById(Long categoryId) {
        if (categoryId == null) return null;
        return categoryRepository.findById(categoryId)
                .map(Category::getName)
                .orElse(null);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Category::getId))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + id));

        try {
            categoryRepository.delete(category);
            categoryRepository.flush();
            log.info("[Category] " + category.getName() + " deleted successfully");
        } catch (DataIntegrityViolationException e) {
            throw new CategoryDeletionException(
                    "Cannot delete category: it has assigned products.",
                    category.getName());
        }
    }

    public void addCategory(Category category) {
        categoryRepository.save(category);
        log.info("[Category] "+category.getName()+" saved successfully");
    }

    @Transactional
    public void editCategory(Long id, String newName) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found: " + id));
        category.setName(newName);
        categoryRepository.save(category);
        log.info("[Category] " + category.getName() + " updated successfully");
    }

    public Category findByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Category not found: " + name));
    }
}

