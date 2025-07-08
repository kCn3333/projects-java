package com.kcn.jobportal.service;

import com.kcn.jobportal.entity.JobSeekerProfile;
import com.kcn.jobportal.entity.RecruiterProfile;
import com.kcn.jobportal.entity.Users;
import com.kcn.jobportal.repository.JobSeekerProfileRepository;
import com.kcn.jobportal.repository.RecruiterProfileRepository;
import com.kcn.jobportal.repository.UsersRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final JobSeekerProfileRepository jobSeekerRepository;
    private final PasswordEncoder passwordEncoder;

    public UsersService(UsersRepository usersRepository, RecruiterProfileRepository recruiterProfileRepository, JobSeekerProfileRepository jobSeekerRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.jobSeekerRepository = jobSeekerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Users addNewUser(Users users){
        users.setActive(true);
        users.setRegistrationDate(new Date(System.currentTimeMillis()));
        users.setPassword(passwordEncoder.encode(users.getPassword()));

        Users savedUsers =usersRepository.save(users);

        if(savedUsers.getUserTypeId().getUserTypeId()==1) {
            System.out.println("recruiter");
            recruiterProfileRepository.save(new RecruiterProfile(savedUsers));
        }
        else {
            System.out.println("jobSeeker");
            jobSeekerRepository.save(new JobSeekerProfile(savedUsers));
        }

        return savedUsers;
    }

    public Optional<Users> getUserByEmail(String email){
        return usersRepository.findByEmail(email);
    }


    public Object getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            String username=authentication.getName();
            Users user=usersRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException(username+" user not found"));

            int userId=user.getUserId();
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
                return recruiterProfileRepository.findById(userId).orElse(new RecruiterProfile());
            }
            else {
                return jobSeekerRepository.findById(userId).orElse(new JobSeekerProfile());
            }
        }
        return null;

    }

    public Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            return usersRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username + " user not found"));
        } else return null;
    }

}
