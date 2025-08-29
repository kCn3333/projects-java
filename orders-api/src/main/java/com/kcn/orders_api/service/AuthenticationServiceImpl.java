package com.kcn.orders_api.service;

import com.kcn.orders_api.dto.request.AuthenticationRequest;
import com.kcn.orders_api.dto.request.RegisterRequest;
import com.kcn.orders_api.dto.response.AuthenticationResponse;
import com.kcn.orders_api.model.Authority;
import com.kcn.orders_api.model.User;
import com.kcn.orders_api.repository.UserRepository;
import com.kcn.orders_api.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }


    @Override
    @Transactional
    public void register(RegisterRequest request) throws Exception {
        if (isEmailTaken(request.getEmail())) {
            throw new Exception("Email is already in use");
        }
        User user=buildNewUser(request);
        userRepository.save(user);

    }

    @Override
    @Transactional(readOnly = true)
    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
        User user=userRepository.findByEmail(request.getEmail()).orElseThrow(()->new IllegalArgumentException("Invalid email or password"));
        String jwtToken = jwtService.generateToken(new HashMap<>(),user);

        return new AuthenticationResponse(jwtToken);
    }

    private boolean isEmailTaken(String email){
        return userRepository.findByEmail(email).isPresent();
    }

    private Set<Authority> initialAuthority() {
        boolean isFirstUser = userRepository.count() == 0;
        Set<Authority> authorities = new HashSet<>();
        authorities.add(new Authority("ROLE_USER"));
        if (isFirstUser) {
            authorities.add(new Authority("ROLE_ADMIN"));
        }
        return authorities;
    }


    private User buildNewUser(RegisterRequest request) {
        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .authorities(initialAuthority())
                .build();
    }
}
