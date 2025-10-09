package com.kcn.e_shop.controller;

import com.kcn.e_shop.dto.CartItem;
import com.kcn.e_shop.service.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("/add/{id}")
    public String addToCart(@PathVariable UUID id,
                            @RequestParam(defaultValue = "1") int quantity,
                            HttpSession session) {
        cartService.addToCart(session, id, quantity);
        return "redirect:/";
    }

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        List<CartItem> cart = cartService.getCartItems(session);
        double total = cartService.getCartTotal(session);

        model.addAttribute("cart", cart);
        model.addAttribute("total", total);
        return "cart";
    }

    @PostMapping("/remove/{id}")
    public String removeFromCart(@PathVariable UUID id, HttpSession session) {
        cartService.removeFromCart(session, id);
        return "redirect:/cart";
    }

    @PostMapping("/update/{id}")
    public String updateQuantity(@PathVariable UUID id,
                                 @RequestParam("quantity") int quantity,
                                 HttpSession session) {
        cartService.updateQuantity(session, id, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(HttpSession session) {
        cartService.clearCart(session);
        return "redirect:/cart";
    }
}