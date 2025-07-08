package com.kcn.jobportal.controller;

import com.kcn.jobportal.entity.*;
import com.kcn.jobportal.service.*;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class JobSeekerApplyController {

    private final JobPostActivityService jobPostActivityService;
    private final UsersService usersService;
    private final JobSeekerApplyService jobSeekerApplyService;
    private final JobSeekerSaveService jobSeekerSaveService;
    private final RecruiterProfileService recruiterProfileService;
    private final JobSeekerProfileService jobSeekerProfileService;

    public JobSeekerApplyController(JobPostActivityService jobPostActivityService, UsersService usersService, JobSeekerApplyService jobSeekerApplyService, JobSeekerSaveService jobSeekerSaveService, RecruiterProfileService recruiterProfileService, JobSeekerProfileService jobSeekerProfileService) {
        this.jobPostActivityService = jobPostActivityService;
        this.usersService = usersService;
        this.jobSeekerApplyService = jobSeekerApplyService;
        this.jobSeekerSaveService = jobSeekerSaveService;
        this.recruiterProfileService = recruiterProfileService;
        this.jobSeekerProfileService = jobSeekerProfileService;
    }

    @GetMapping("/job-details-apply/{id}")
    public String displayDetails(@PathVariable("id") int id, Model model){
        JobPostActivity jobDetails= jobPostActivityService.getJobById(id);
        List<JobSeekerApply> jobSeekerApplyList=jobSeekerApplyService.getJobCandidates(jobDetails);
        List<JobSeekerSave> jobSeekerSaveList=jobSeekerSaveService.getJobCandidates(jobDetails);

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))){
                RecruiterProfile user=recruiterProfileService.getCurrentRecruiterProfile();
                if(user !=null){
                    model.addAttribute("applyList",jobSeekerApplyList);
                }
            } else{
                JobSeekerProfile user=jobSeekerProfileService.getCurrentJobSeekerProfile();
                if(user !=null){
                    boolean exist=false;
                    boolean save=false;
                    for(JobSeekerApply job: jobSeekerApplyList){
                        if(Objects.equals(job.getUserId().getUserAccountId(), user.getUserAccountId())){
                            exist=true;
                            break;
                        }
                    }
                    for(JobSeekerSave job: jobSeekerSaveList){
                        if(Objects.equals(job.getUserId().getUserAccountId(), user.getUserAccountId())){
                            save=true;
                            break;
                        }
                    }
                    model.addAttribute("alreadyApplied",exist);
                    model.addAttribute("alreadySaved", save);

                }
            }
        }
        JobSeekerApply jobSeekerApply= new JobSeekerApply();
        model.addAttribute("applyJob", jobSeekerApply);

        model.addAttribute("jobDetails",jobDetails);
        model.addAttribute("user", usersService.getCurrentUserProfile());
        return "job-details";
    }

    @PostMapping("job-details/apply/{id}")
    public String apply(@PathVariable("id") int id, JobSeekerApply jobSeekerApply){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUsername=authentication.getName();
            Users user=usersService.getUserByEmail(currentUsername).orElseThrow(()-> new UsernameNotFoundException("User not found"));
            Optional<JobSeekerProfile> seekerProfile = jobSeekerProfileService.getProfile(user.getUserId());
            JobPostActivity jobPostActivity= jobPostActivityService.getJobById(id);
            if(seekerProfile.isPresent() && jobPostActivity!=null){
                jobSeekerApply = new JobSeekerApply();
                jobSeekerApply.setUserId(seekerProfile.get());
                jobSeekerApply.setJob(jobPostActivity);
                jobSeekerApply.setApplyDate(new Date());
            } else {
                throw new RuntimeException("User not found");
            }
            jobSeekerApplyService.addNew(jobSeekerApply);

        }
        return "redirect:/dashboard/";
    }


}
