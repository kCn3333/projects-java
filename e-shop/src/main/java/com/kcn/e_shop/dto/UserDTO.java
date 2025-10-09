package com.kcn.e_shop.dto;

import com.kcn.e_shop.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserDTO(
        Long id,

        @NotBlank(message = "Username cannot be empty")
        @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
        String username,

        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password cannot be empty")
        @Size(min = 3, max = 20, message = "Password must be at least 3 characters")
        String password,

        Role role
) {
    // Factory method for empty form
    public static UserDTO empty() {
        return new UserDTO(null, "", "", "", Role.USER);
    }

    // Factory method for preserving form data (without password)
    public static UserDTO withData(String username, String email, Role role) {
        return new UserDTO(null, username, email, "", role);
    }
    // Factory method for admin user with parameters
    public static UserDTO admin(String username, String password, String email) {
        return new UserDTO(null, username, email, password, Role.ADMIN);
    }

}
