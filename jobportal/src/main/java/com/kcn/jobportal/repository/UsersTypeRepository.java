package com.kcn.jobportal.repository;


import com.kcn.jobportal.entity.Users;
import com.kcn.jobportal.entity.UsersType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersTypeRepository extends JpaRepository<UsersType, Integer> {


}
