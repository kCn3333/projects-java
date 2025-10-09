package com.kcn.e_shop.controller;

import com.kcn.e_shop.dto.OrderDTO;
import com.kcn.e_shop.entity.OrderStatus;
import com.kcn.e_shop.service.OrderService;
import com.kcn.e_shop.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @PostMapping("/order/create")
    public String createOrder(Authentication authentication, HttpSession session) {
        String username = authentication.getName();
        Long userId = userService.getUserIdByUsername(username);

        OrderDTO orderDTO = orderService.createOrderFromCart(session, userId);
        return "redirect:/order/confirmation?orderId=" + orderDTO.id();
    }

    @GetMapping("/order/confirmation")
    public String orderConfirmation(@RequestParam Long orderId, Authentication authentication, Model model) {
        Long userId = userService.getUserIdByUsername(authentication.getName());
        OrderDTO orderDTO = orderService.getOrderById(orderId,userId);

        model.addAttribute("order", orderDTO);
        return "order-confirmation";
    }

    @GetMapping("/orders")
    public String getUserOrders(Authentication authentication, Model model) {
        String username = authentication.getName();
        Long userId = userService.getUserIdByUsername(username);

        List<OrderDTO> orders = orderService.getUserOrders(userId);
        model.addAttribute("orders", orders);
        return "user-orders";
    }

    @GetMapping("/admin/orders")
    public String getAllOrders(Model model) {
        List<OrderDTO> orders = orderService.getAllOrders();

        model.addAttribute("orders", orders);
        return "admin-orders";
    }

    @PostMapping("/admin/orders/update-status")
    public String updateOrderStatus(@RequestParam Long orderId, @RequestParam OrderStatus status, RedirectAttributes redirectAttributes) {
        orderService.updateOrderStatus(orderId, status);
        redirectAttributes.addFlashAttribute("successMessage", "Order status updated");
        return "redirect:/admin/orders";
    }
}