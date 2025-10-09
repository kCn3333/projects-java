package com.kcn.e_shop.controller;

import com.kcn.e_shop.dto.LoginDTO;
import com.kcn.e_shop.dto.UserDTO;
import com.kcn.e_shop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/admin/users")
    public String getAllUsers(Model model) {
        List<UserDTO> users = userService.findAllUsers();

        model.addAttribute("users", users);
        return "admin-users";
    }

    @PostMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully");
            return "redirect:/admin/users";
        } catch (IllegalStateException | IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/users";
        }
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userDTO", UserDTO.empty());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userDTO") UserDTO userDTO,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            userService.registerUser(userDTO);
            return "redirect:/login?success";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("userDTO", UserDTO.withData(
                    userDTO.username(),
                    userDTO.email(),
                    userDTO.role()
            ));
            return "register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {

        // Add error message if login failed
        if (error != null) {
            model.addAttribute("error", "Invalid username or password!");
        }

        // Add message if logged out successfully
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully.");
        }

        model.addAttribute("loginDTO", LoginDTO.empty());
        return "login";
    }
}
