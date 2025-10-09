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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final CartService cartService;

    @GetMapping("/")
    public String index(@RequestParam(required = false) String q,
                        @RequestParam(defaultValue = "1") int topK,
                        Model model,
                        HttpSession session) {

        List<ProductDTO> products;
        if (q == null || q.isEmpty()) {
            products = productService.findAll();
        } else {
            products = productService.search(q);
        }

        model.addAttribute("products", products);

        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("newCategory", new Category());

        model.addAttribute("cartCount", cartService.getCartItemCount(session));

        return "index";
    }

}