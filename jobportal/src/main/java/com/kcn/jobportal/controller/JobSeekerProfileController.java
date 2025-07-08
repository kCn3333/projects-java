package com.kcn.jobportal.controller;

import com.kcn.jobportal.entity.JobSeekerProfile;
import com.kcn.jobportal.entity.RecruiterProfile;
import com.kcn.jobportal.entity.Skills;
import com.kcn.jobportal.entity.Users;
import com.kcn.jobportal.repository.UsersRepository;
import com.kcn.jobportal.service.JobSeekerProfileService;
import com.kcn.jobportal.util.FileDownloadUtil;
import com.kcn.jobportal.util.FileUploadUtil;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("job-seeker-profile")
public class JobSeekerProfileController {

    private JobSeekerProfileService jobSeekerProfileService;
    private UsersRepository usersRepository;

    public JobSeekerProfileController(JobSeekerProfileService jobSeekerProfileService, UsersRepository usersRepository) {
        this.jobSeekerProfileService = jobSeekerProfileService;
        this.usersRepository = usersRepository;
    }

    @GetMapping("/")
    public String showJobSeekerProfile(Model model){
        JobSeekerProfile jobSeekerProfile=new JobSeekerProfile();
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        List<Skills> skills=new ArrayList<>();

        if (!(authentication instanceof AnonymousAuthenticationToken)){
            Users user=usersRepository.findByEmail(authentication.getName()).orElseThrow(()-> new UsernameNotFoundException("User not found"));
            Optional<JobSeekerProfile> seekerProfile = jobSeekerProfileService.getProfile(user.getUserId());
            if(seekerProfile.isPresent()){
                jobSeekerProfile=seekerProfile.get();
                if(jobSeekerProfile.getSkills().isEmpty()) {
                    skills.add(new Skills());
                    jobSeekerProfile.setSkills(skills);
                }
            }
            model.addAttribute("skills",skills);
            model.addAttribute("profile",jobSeekerProfile);
        }
        return "job-seeker-profile";
    }

    @PostMapping("/addNew")
    public String addNew(JobSeekerProfile jobSeekerProfile, @RequestParam("image") MultipartFile image,
                         @RequestParam("pdf") MultipartFile resume, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            Users user = usersRepository.findByEmail(currentUsername)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            jobSeekerProfile.setUserId(user);
            jobSeekerProfile.setUserAccountId(user.getUserId());
        }

        for (Skills skill : jobSeekerProfile.getSkills()) {
            skill.setJobSeekerProfile(jobSeekerProfile);
        }

        // Ustaw nazwę plików, jeśli przesłane
        if (image != null && !image.isEmpty()) {
            String imageName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
            jobSeekerProfile.setProfilePhoto(imageName);
        }
        if (resume != null && !resume.isEmpty()) {
            String resumeName = StringUtils.cleanPath(Objects.requireNonNull(resume.getOriginalFilename()));
            jobSeekerProfile.setResume(resumeName);
        }

        // Teraz zapis profilu
        JobSeekerProfile savedJobSeekerProfile = jobSeekerProfileService.addNew(jobSeekerProfile);

        // Dopiero po zapisie zapisujemy pliki
        String uploadDirectory = "photos/candidate/" + savedJobSeekerProfile.getUserAccountId();

        if (image != null && !image.isEmpty()) {
            try {
                FileUploadUtil.saveFile(uploadDirectory, savedJobSeekerProfile.getProfilePhoto(), image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (resume != null && !resume.isEmpty()) {
            try {
                FileUploadUtil.saveFile(uploadDirectory, savedJobSeekerProfile.getResume(), resume);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "redirect:/dashboard/";
    }

    @GetMapping("/{id}")
    public String candidateProfile(@PathVariable("id") int id, Model model){

        JobSeekerProfile profile = jobSeekerProfileService.getProfile(id).orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("profile",profile);
        return "job-seeker-profile";

    }

    @GetMapping("/downloadResume")
    public ResponseEntity<?> downloadResume(@RequestParam(value = "fileName") String fileName, @RequestParam(value="userID") String userId){
        FileDownloadUtil fileDownloadUtil=new FileDownloadUtil();
        Resource resource=null;
        try {
            resource =fileDownloadUtil.getFileAsResource("photos/candidate/"+userId, fileName);
        }catch (IOException e){
            return ResponseEntity.badRequest().build();
        }

        if(resource==null){
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }
        String contentType="application/octet-stream";
        String headerValue="attachment; filename=\""+resource.getFilename()+"\"";

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).header(HttpHeaders.CONTENT_DISPOSITION,headerValue).body(resource);
    }


}
