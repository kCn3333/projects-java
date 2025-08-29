package com.kcn.orders_api.service;

import com.kcn.orders_api.dto.request.PasswordUpdateRequest;
import com.kcn.orders_api.dto.response.UserResponse;
import com.kcn.orders_api.model.Authority;
import com.kcn.orders_api.model.User;
import com.kcn.orders_api.repository.UserRepository;
import com.kcn.orders_api.util.AuthenticationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final AuthenticationUtils authenticationUtils;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, AuthenticationUtils authenticationUtils, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationUtils = authenticationUtils;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserInfo(){
        User currentUser=authenticationUtils.getCurrentUser();
        return UserResponse.builder()
                .id(currentUser.getId())
                .fullName(currentUser.getFirstName() + " " + currentUser.getLastName())
                .email(currentUser.getEmail())
                .authorities(
                        currentUser.getAuthorities().stream()
                                .map(a -> ((Authority) a).getAuthority())
                                .collect(Collectors.toList())
                )
                .build();
    }

    @Override
    @Transactional
    public void deleteUser() {
        User currentUser=authenticationUtils.getCurrentUser();
        if (isLastAdmin(currentUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Cannot delete the last admin user");
        }

        userRepository.delete(currentUser);
    }

    @Override
    @Transactional
    public void updatePassword(PasswordUpdateRequest passwordUpdateRequest) {
        User user=authenticationUtils.getCurrentUser();

        if (!isOldPasswordCorrect(passwordUpdateRequest.getOldPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current password is incorrect");
        }
        if(!isNewPasswordConfirmed(passwordUpdateRequest.getNewPassword(),passwordUpdateRequest.getNewPassword2())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"New passwords do not match");
        }
        if(!isNewPasswordDifferent(passwordUpdateRequest.getNewPassword(),passwordUpdateRequest.getOldPassword())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"New password must be different");
        }

        user.setPassword(passwordEncoder.encode(passwordUpdateRequest.getNewPassword()));
        userRepository.save(user);
    }

    private boolean isOldPasswordCorrect(String oldPassword, String currentPassword) {
        return passwordEncoder.matches(oldPassword, currentPassword);
    }
    private boolean isNewPasswordConfirmed(String newPassword, String newPassword2){
        return newPassword.equals(newPassword2);
    }
    private boolean isNewPasswordDifferent(String newPassword, String oldPassword){
        return !newPassword.equals(oldPassword);
    }

    private boolean isLastAdmin(User user){
        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return false; // non Admin user can be deleted
        }

        long adminCount = userRepository.countAdminUsers();
        return adminCount<=1; // last admin cant be deleted
    }
}
