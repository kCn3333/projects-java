package com.kcn.jobportal.service;

import com.kcn.jobportal.entity.Users;
import com.kcn.jobportal.repository.UsersRepository;
import com.kcn.jobportal.util.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;

    public CustomUserDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user=usersRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("Could not find the user"));
        return new CustomUserDetails(user);
    }
}
