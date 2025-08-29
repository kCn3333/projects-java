package com.kcn.orders_api.service;

import com.kcn.orders_api.dto.request.AuthenticationRequest;
import com.kcn.orders_api.dto.request.RegisterRequest;
import com.kcn.orders_api.dto.response.AuthenticationResponse;

public interface AuthenticationService {
    void register(RegisterRequest request) throws Exception;
    AuthenticationResponse login(AuthenticationRequest request);
}
