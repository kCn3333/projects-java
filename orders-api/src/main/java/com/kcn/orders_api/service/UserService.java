package com.kcn.orders_api.service;

import com.kcn.orders_api.dto.request.PasswordUpdateRequest;
import com.kcn.orders_api.dto.response.UserResponse;

public interface UserService {
    UserResponse getUserInfo();
    void deleteUser();
    void updatePassword(PasswordUpdateRequest passwordUpdateRequest);
}
