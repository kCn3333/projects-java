package com.kcn.jobportal.controller;


import com.kcn.jobportal.entity.RecruiterProfile;
import com.kcn.jobportal.entity.Users;
import com.kcn.jobportal.repository.UsersRepository;
import com.kcn.jobportal.service.RecruiterProfileService;
import com.kcn.jobportal.util.FileUploadUtil;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/recruiter-profile")
public class RecruiterProfileController {

    private final UsersRepository usersRepository;
    private final RecruiterProfileService recruiterProfileService;

    public RecruiterProfileController(UsersRepository usersRepository, RecruiterProfileService recruiterProfileService) {
        this.usersRepository = usersRepository;
        this.recruiterProfileService = recruiterProfileService;
    }

    @GetMapping("/")
    public String showRecruiterProfile(Model model){

        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUsername=authentication.getName();
            Users user=usersRepository.findByEmail(currentUsername).orElseThrow(()->new UsernameNotFoundException("user not found"));
            Optional<RecruiterProfile> recruiterProfile =recruiterProfileService.getRecruiterProfile(user.getUserId());

            if(recruiterProfile.isPresent()){
                model.addAttribute("profile",recruiterProfile);
            }
        }

        return "recruiter_profile";
    }

    @PostMapping("/addNew")
    public String addNew(RecruiterProfile recruiterProfile, @RequestParam("image") MultipartFile multipartFile, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            Users user = usersRepository.findByEmail(currentUsername)
                    .orElseThrow(() -> new UsernameNotFoundException("user not found"));
            recruiterProfile.setUserId(user);
            recruiterProfile.setUserAccountId(user.getUserId());
        }

        model.addAttribute("profile", recruiterProfile);

        if (multipartFile != null && !multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            recruiterProfile.setProfilePhoto(fileName);

            RecruiterProfile savedRecruiterProfile = recruiterProfileService.addNew(recruiterProfile);
            String uploadDirectory = "photos/recruiter/" + savedRecruiterProfile.getUserAccountId();

            try {
                FileUploadUtil.saveFile(uploadDirectory, fileName, multipartFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "redirect:/dashboard/";
        }

        recruiterProfileService.addNew(recruiterProfile);
        return "redirect:/dashboard/";
    }





}
