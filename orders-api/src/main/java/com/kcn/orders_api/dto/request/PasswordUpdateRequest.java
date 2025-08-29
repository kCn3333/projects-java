package com.kcn.orders_api.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
public class PasswordUpdateRequest {
    @NotEmpty(message="Old password is mandatory")
    @Size(min = 3, max = 64, message = "Password must be between 3 and 64 characters")
    private String oldPassword;

    @NotEmpty(message = "New password is mandatory")
    @Size(min = 3, max = 64, message = "Password must be between 3 and 64 characters")
    private String newPassword;

    @NotEmpty(message = "Confirmed password is mandatory")
    @Size(min = 3, max = 64, message = "Password must be between 3 and 64 characters")
    private String newPassword2;

}
