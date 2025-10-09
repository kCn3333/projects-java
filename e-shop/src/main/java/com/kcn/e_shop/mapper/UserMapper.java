package com.kcn.e_shop.mapper;

import com.kcn.e_shop.dto.UserDTO;
import com.kcn.e_shop.entity.Role;
import com.kcn.e_shop.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                null,
                user.getRole()
        );
    }

    public User toEntity(UserDTO dto, String encodedPassword) {
        return User.builder()
                .id(dto.id())
                .username(dto.username())
                .email(dto.email())
                .password(encodedPassword)
                .role(dto.role() != null ? dto.role() : Role.USER)
                .build();
    }

    public void updateEntity(User user, UserDTO dto, String encodedPassword) {
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setRole(dto.role());
        if (encodedPassword != null) {
            user.setPassword(encodedPassword);
        }
    }
}
