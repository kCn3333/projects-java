package com.kcn.jobportal.controller;

import com.kcn.jobportal.entity.*;
import com.kcn.jobportal.repository.UsersRepository;
import com.kcn.jobportal.service.JobPostActivityService;
import com.kcn.jobportal.service.JobSeekerProfileService;
import com.kcn.jobportal.service.JobSeekerSaveService;
import com.kcn.jobportal.service.UsersService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class JobSeekerSaveController {

    private final UsersService usersService;
    private final JobSeekerProfileService jobSeekerProfileService;
    private final JobPostActivityService jobPostActivityService;
    private final JobSeekerSaveService jobSeekerSaveService;

    public JobSeekerSaveController(UsersService usersService, JobSeekerProfileService jobSeekerProfileService, JobPostActivityService jobPostActivityService, JobSeekerSaveService jobSeekerSaveService) {
        this.usersService = usersService;
        this.jobSeekerProfileService = jobSeekerProfileService;
        this.jobPostActivityService = jobPostActivityService;
        this.jobSeekerSaveService = jobSeekerSaveService;
    }

    @PostMapping("job-details/save/{id}")
    public String save(@PathVariable("id") int id, JobSeekerSave jobSeekerSave){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUsername=authentication.getName();
            Users user=usersService.getUserByEmail(currentUsername).orElseThrow(()-> new UsernameNotFoundException("User not found"));
            Optional<JobSeekerProfile> seekerProfile = jobSeekerProfileService.getProfile(user.getUserId());
            JobPostActivity jobPostActivity= jobPostActivityService.getJobById(id);
            if(seekerProfile.isPresent() && jobPostActivity!=null){
                jobSeekerSave = new JobSeekerSave();
                jobSeekerSave.setUserId(seekerProfile.get());
                jobSeekerSave.setJob(jobPostActivity);
            } else {
                throw new RuntimeException("User not found");
            }
            jobSeekerSaveService.addNew(jobSeekerSave);

        }
        return "redirect:/dashboard/";
    }

    @GetMapping("saved-jobs/")
    public String savedJobs(Model model){
        List<JobPostActivity> jobPostList=new ArrayList<>();
        Object currentUserProfile = usersService.getCurrentUserProfile();

        List<JobSeekerSave> jobSeekerSavedList = jobSeekerSaveService.getCandidatesJob((JobSeekerProfile) currentUserProfile);
        for(JobSeekerSave job: jobSeekerSavedList){
            jobPostList.add(job.getJob());
        }
        model.addAttribute("jobPost",jobPostList);
        model.addAttribute("user",currentUserProfile);

        return "saved-jobs";
    }

}
