package com.kcn.e_shop.controller;

import com.kcn.e_shop.dto.ProductDTO;
import com.kcn.e_shop.entity.Category;

import com.kcn.e_shop.service.CartService;
import com.kcn.e_shop.service.CategoryService;
import com.kcn.e_shop.service.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final CartService cartService;

    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        categoryService.deleteCategory(id);
        redirectAttributes.addFlashAttribute("successMessage", "Category and all its products were deleted!");
        return "redirect:/";
    }

    @PostMapping("/add")
    public String addCategory(@ModelAttribute("newCategory") Category category, RedirectAttributes redirectAttributes) {
        categoryService.addCategory(category);
        redirectAttributes.addFlashAttribute("successMessage", "Category added successfully!");
        return "redirect:/";
    }

    @PostMapping("/edit")
    public String editCategory(@RequestParam Long id, @RequestParam String name, RedirectAttributes redirectAttributes) {
        categoryService.editCategory(id, name);
        redirectAttributes.addFlashAttribute("successMessage", "Category edited successfully!");
        return "redirect:/";
    }

    @GetMapping("/{name}")
    public String showCategory(@PathVariable String name, Model model, HttpSession session) {
        Category category = categoryService.findByName(name);
        List<ProductDTO> products = productService.findAll();

        model.addAttribute("products", products);
        model.addAttribute("categories", List.of(category));
        model.addAttribute("newCategory", new Category());
        model.addAttribute("cartCount", cartService.getCartItemCount(session));

        return "index";
    }

}
