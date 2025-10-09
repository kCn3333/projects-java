package com.kcn.e_shop.service;

import com.kcn.e_shop.config.AdminConfig;
import com.kcn.e_shop.dto.UserDTO;
import com.kcn.e_shop.entity.Role;
import com.kcn.e_shop.entity.User;
import com.kcn.e_shop.exception.AdminOperationException;
import com.kcn.e_shop.mapper.UserMapper;
import com.kcn.e_shop.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AdminConfig adminConfig;

    @PostConstruct
    @Transactional
    public void initAdmin() {
        try {
            // Check if admin already exists
            if (findByUsername(adminConfig.getUsername()).isEmpty()) {
                UserDTO admin = UserDTO.admin(
                        adminConfig.getUsername(),
                        adminConfig.getPassword(),
                        adminConfig.getEmail()
                );

                registerUser(admin);
                log.info("[Admin] account for {} successfully created", admin.username());
            } else {
                log.info("[Admin] admin account already exists");
            }
        } catch (Exception e) {
            log.error("[Admin] Failed to create admin account: {}", e.getMessage());
        }
    }

    @Transactional
    public UserDTO registerUser(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.username())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(userDTO.email())) {
            throw new IllegalArgumentException("Email already registered");
        }

        String encodedPassword = passwordEncoder.encode(userDTO.password());
        User user = userMapper.toEntity(userDTO, encodedPassword);

        User saved = userRepository.save(user);
        return userMapper.toDTO(saved);
    }

    public Optional<UserDTO> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toDTO);
    }

    public Optional<UserDTO> findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDTO);
    }

    public Long getUserIdByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public User getUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public User getUserEntityByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public List<UserDTO> findAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!user.getUsername().equals(userDTO.username()) &&
                userRepository.existsByUsername(userDTO.username())) {
            throw new IllegalArgumentException("Username already taken");
        }

        if (!user.getEmail().equals(userDTO.email()) &&
                userRepository.existsByEmail(userDTO.email())) {
            throw new IllegalArgumentException("Email already registered");
        }

        String encodedPassword = null;
        if (userDTO.password() != null && !userDTO.password().trim().isEmpty()) {
            encodedPassword = passwordEncoder.encode(userDTO.password());
        }

        userMapper.updateEntity(user, userDTO, encodedPassword);

        User updated = userRepository.save(user);
        return userMapper.toDTO(updated);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AdminOperationException("User not found with id: " + id));

        if (user.getRole() == Role.ADMIN) {
            throw new AdminOperationException("Cannot delete admin users");
        }

        userRepository.delete(user);
        log.info("[Admin] " + user.getUsername() + " deleted successfully");
    }

}