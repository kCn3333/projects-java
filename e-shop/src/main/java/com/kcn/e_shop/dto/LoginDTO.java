package com.kcn.e_shop.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
        @NotBlank(message = "Username cannot be empty")
        String username,

        @NotBlank(message = "Password cannot be empty")
        String password
) {
    // Add a default constructor for the record
    public LoginDTO {
    }

    // Static factory method for empty instance
    public static LoginDTO empty() {
        return new LoginDTO("", "");
    }
}