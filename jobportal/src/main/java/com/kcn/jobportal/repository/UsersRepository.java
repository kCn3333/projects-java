package com.kcn.jobportal.repository;
import com.kcn.jobportal.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users,Integer> {

    public Optional<Users> findByEmail(String email);
}
