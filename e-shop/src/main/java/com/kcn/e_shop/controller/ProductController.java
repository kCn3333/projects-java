package com.kcn.e_shop.controller;

import com.kcn.e_shop.dto.ProductDTO;
import com.kcn.e_shop.service.CartService;
import com.kcn.e_shop.service.CategoryService;
import com.kcn.e_shop.service.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final CartService cartService;

    @GetMapping("/new")
    public String showAddProductForm(@RequestParam(required = false) Long categoryId, Model model) {
        String categoryName = categoryService.getCategoryNameById(categoryId);
        ProductDTO product = ProductDTO.emptyWithCategory(categoryName);

        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.findAll());
        return "add-product";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        productService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Product removed successfully!");
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable UUID id, Model model) {
        ProductDTO product = productService.findById(id);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.findAll());
        return "add-product";
    }

    @PostMapping({"/", "/{id}"})
    public String saveOrUpdateProduct(@PathVariable(required = false) UUID id,
                                      @Valid @ModelAttribute ProductDTO product,
                                      BindingResult bindingResult,
                                      @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {

        if (bindingResult.hasErrors()) {
            // Ensure product is never null
            if (product == null) {
                product = ProductDTO.emptyWithCategory(null);
            }
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryService.findAll());
            return "add-product";
        }

        try {
            productService.saveOrUpdate(product, imageFile);
            redirectAttributes.addFlashAttribute("successMessage", "Product saved successfully!");
            return "redirect:/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
            model.addAttribute("categories", categoryService.findAll());
            return "add-product";
        }
    }

    @GetMapping("/{id}")
    public String getProduct(@PathVariable UUID id, Model model, HttpSession session) {
        ProductDTO product = productService.findById(id);
        model.addAttribute("product", product);

        model.addAttribute("cartCount", cartService.getCartItemCount(session));

        return "product";
    }

    @PostMapping("/generate-description")
    @ResponseBody
    public ResponseEntity<String> generateDescription(@RequestParam String productName,
                                                      @RequestParam String categoryName) {
        String response = productService.generateDescription(productName, categoryName);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/generate-features")
    @ResponseBody
    public ResponseEntity<String> generateFeatures(@RequestParam String productName,
                                                   @RequestParam String categoryName) {
        String response = productService.generateFeatures(productName, categoryName);
        return ResponseEntity.ok(response);
    }
}


