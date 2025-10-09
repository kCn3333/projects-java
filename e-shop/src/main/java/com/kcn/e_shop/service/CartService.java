package com.kcn.e_shop.service;

import com.kcn.e_shop.dto.CartItem;
import com.kcn.e_shop.entity.Product;
import com.kcn.e_shop.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ProductRepository productRepository;

    public List<CartItem> getCartItems(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        return cart != null ? cart : new ArrayList<>();
    }

    public void addToCart(HttpSession session, UUID productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        List<CartItem> cart = getCartItems(session);

        boolean found = false;
        for (CartItem item : cart) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(item.getQuantity() + quantity);
                found = true;
                break;
            }
        }

        if (!found) {
            cart.add(new CartItem(product, quantity));
        }

        session.setAttribute("cart", cart);
    }

    public void updateQuantity(HttpSession session, UUID productId, int quantity) {
        List<CartItem> cart = getCartItems(session);

        for (CartItem item : cart) {
            if (item.getProduct().getId().equals(productId)) {
                if (quantity <= 0) {
                    cart.remove(item);
                } else {
                    item.setQuantity(quantity);
                }
                break;
            }
        }

        session.setAttribute("cart", cart);
    }

    public void removeFromCart(HttpSession session, UUID productId) {
        List<CartItem> cart = getCartItems(session);
        cart.removeIf(item -> item.getProduct().getId().equals(productId));
        session.setAttribute("cart", cart);
    }

    public void clearCart(HttpSession session) {
        session.removeAttribute("cart");
    }

    public double getCartTotal(HttpSession session) {
        List<CartItem> cart = getCartItems(session);
        return cart.stream()
                .mapToDouble(item -> item.getProduct().getPrice().doubleValue() * item.getQuantity())
                .sum();
    }

    public int getCartItemCount(HttpSession session) {
        List<CartItem> cart = getCartItems(session);
        return cart.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
}