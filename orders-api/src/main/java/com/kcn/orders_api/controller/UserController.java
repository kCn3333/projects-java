package com.kcn.orders_api.controller;

import com.kcn.orders_api.dto.request.PasswordUpdateRequest;
import com.kcn.orders_api.dto.response.ApiResponse;
import com.kcn.orders_api.dto.response.UserResponse;
import com.kcn.orders_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name="User REST API Endpoints", description ="Operations related to user data")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get current user info", description = "Returns info about the authenticated user", security = @SecurityRequirement(name ="bearerAuth"))
    @GetMapping("/info")
    public ResponseEntity<UserResponse> info() {
        UserResponse userResponse = userService.getUserInfo();
        return ResponseEntity.ok(userResponse);
    }

    @Operation(summary = "Delete current user", description = "Deletes the authenticated user", security = @SecurityRequirement(name ="bearerAuth"))
    @DeleteMapping
    public ResponseEntity<ApiResponse> deleteUser(){
        userService.deleteUser();
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .message("User deleted successfully")
                        .build()
        );
    }

    @Operation(summary = "Update password", description = "Allows the authenticated user to change password", security = @SecurityRequirement(name ="bearerAuth"))
    @PutMapping("/password")
    public ResponseEntity<ApiResponse> updatePassword(@Valid @RequestBody PasswordUpdateRequest request) {
        userService.updatePassword(request);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .message("Password updated successfully")
                        .build());
    }



}
