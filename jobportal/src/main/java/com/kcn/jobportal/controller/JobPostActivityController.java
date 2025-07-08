package com.kcn.jobportal.controller;

import com.kcn.jobportal.entity.*;
import com.kcn.jobportal.service.JobPostActivityService;
import com.kcn.jobportal.service.JobSeekerApplyService;
import com.kcn.jobportal.service.JobSeekerSaveService;
import com.kcn.jobportal.service.UsersService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class JobPostActivityController {

    private final UsersService usersService;
    private final JobPostActivityService jobPostActivityService;
    private final JobSeekerApplyService jobSeekerApplyService;
    private final JobSeekerSaveService jobSeekerSaveService;

    public JobPostActivityController(UsersService usersService, JobPostActivityService jobPostActivityService, JobSeekerApplyService jobSeekerApplyService, JobSeekerSaveService jobSeekerSaveService) {
        this.usersService = usersService;
        this.jobPostActivityService = jobPostActivityService;
        this.jobSeekerApplyService = jobSeekerApplyService;
        this.jobSeekerSaveService = jobSeekerSaveService;
    }

    @GetMapping("global-search/")
    public String globalSearch(Model model,
                               @RequestParam(value = "job", required = false) String job,
                               @RequestParam(value = "location", required = false) String location,
                               @RequestParam(value = "partTime", required = false) String partTime,
                               @RequestParam(value = "fullTime", required = false) String fullTime,
                               @RequestParam(value = "freelance", required = false) String freelance,
                               @RequestParam(value = "remoteOnly", required = false) String remoteOnly,
                               @RequestParam(value = "officeOnly", required = false) String officeOnly,
                               @RequestParam(value = "partialRemote", required = false) String partialRemote,
                               @RequestParam(value = "today", required = false) boolean today,
                               @RequestParam(value = "days7", required = false) boolean days7,
                               @RequestParam(value = "days30", required = false) boolean days30
    ){
        model.addAttribute("partTime", Objects.equals(partTime, "Part-Time"));
        model.addAttribute("fullTime", Objects.equals(fullTime, "Full-Time"));
        model.addAttribute("freelance", Objects.equals(freelance, "Freelance"));

        model.addAttribute("remoteOnly", Objects.equals(remoteOnly, "Remote-Only"));
        model.addAttribute("officeOnly", Objects.equals(officeOnly, "Office-Only"));
        model.addAttribute("partialRemote", Objects.equals(partialRemote, "Partial-Remote"));

        model.addAttribute("today", today);
        model.addAttribute("days7", days7);
        model.addAttribute("days30", days30);

        model.addAttribute("job", job);
        model.addAttribute("location", location);

        LocalDate searchDate = null;
        List<JobPostActivity> jobPost = null;
        boolean dateSearchFlag = true;

        if (days30) {
            searchDate = LocalDate.now().minusDays(30);
        } else if (days7) {
            searchDate = LocalDate.now().minusDays(7);
        } else if (today) {
            searchDate = LocalDate.now();
        } else {
            dateSearchFlag = false;
        }

        List<String> jobTypes = new ArrayList<>();
        if ("Part-Time".equals(partTime)) jobTypes.add("Part-Time");
        if ("Full-Time".equals(fullTime)) jobTypes.add("Full-Time");
        if ("Freelance".equals(freelance)) jobTypes.add("Freelance");

        List<String> remoteTypes = new ArrayList<>();
        if ("Remote-Only".equals(remoteOnly)) remoteTypes.add("Remote-Only");
        if ("Office-Only".equals(officeOnly)) remoteTypes.add("Office-Only");
        if ("Partial-Remote".equals(partialRemote)) remoteTypes.add("Partial-Remote");

        jobPost = jobPostActivityService.search(
                StringUtils.hasText(job) ? job : null,
                StringUtils.hasText(location) ? location : null,
                jobTypes.isEmpty() ? null : jobTypes,
                remoteTypes.isEmpty() ? null : remoteTypes,
                searchDate
        );
        model.addAttribute("jobPost", jobPost);
        return "global-search";


}

    @GetMapping("/dashboard/")
    public String searchJobs(Model model,
                             @RequestParam(value = "job", required = false) String job,
                             @RequestParam(value = "location", required = false) String location,
                             @RequestParam(value = "partTime", required = false) String partTime,
                             @RequestParam(value = "fullTime", required = false) String fullTime,
                             @RequestParam(value = "freelance", required = false) String freelance,
                             @RequestParam(value = "remoteOnly", required = false) String remoteOnly,
                             @RequestParam(value = "officeOnly", required = false) String officeOnly,
                             @RequestParam(value = "partialRemote", required = false) String partialRemote,
                             @RequestParam(value = "today", required = false) boolean today,
                             @RequestParam(value = "days7", required = false) boolean days7,
                             @RequestParam(value = "days30", required = false) boolean days30
    ) {

        model.addAttribute("partTime", Objects.equals(partTime, "Part-Time"));
        model.addAttribute("fullTime", Objects.equals(fullTime, "Full-Time"));
        model.addAttribute("freelance", Objects.equals(freelance, "Freelance"));

        model.addAttribute("remoteOnly", Objects.equals(remoteOnly, "Remote-Only"));
        model.addAttribute("officeOnly", Objects.equals(officeOnly, "Office-Only"));
        model.addAttribute("partialRemote", Objects.equals(partialRemote, "Partial-Remote"));

        model.addAttribute("today", today);
        model.addAttribute("days7", days7);
        model.addAttribute("days30", days30);

        model.addAttribute("job", job);
        model.addAttribute("location", location);

        LocalDate searchDate = null;
        List<JobPostActivity> jobPost = null;
        boolean dateSearchFlag = true;

        if (days30) {
            searchDate = LocalDate.now().minusDays(30);
        } else if (days7) {
            searchDate = LocalDate.now().minusDays(7);
        } else if (today) {
            searchDate = LocalDate.now();
        } else {
            dateSearchFlag = false;
        }

        List<String> jobTypes = new ArrayList<>();
        if ("Part-Time".equals(partTime)) jobTypes.add("Part-Time");
        if ("Full-Time".equals(fullTime)) jobTypes.add("Full-Time");
        if ("Freelance".equals(freelance)) jobTypes.add("Freelance");

        List<String> remoteTypes = new ArrayList<>();
        if ("Remote-Only".equals(remoteOnly)) remoteTypes.add("Remote-Only");
        if ("Office-Only".equals(officeOnly)) remoteTypes.add("Office-Only");
        if ("Partial-Remote".equals(partialRemote)) remoteTypes.add("Partial-Remote");

        jobPost = jobPostActivityService.search(
                StringUtils.hasText(job) ? job : null,
                StringUtils.hasText(location) ? location : null,
                jobTypes.isEmpty() ? null : jobTypes,
                remoteTypes.isEmpty() ? null : remoteTypes,
                searchDate
        );

        Object currentUserProfile = usersService.getCurrentUserProfile();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            model.addAttribute("username", authentication.getName());

            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
                List<RecruiterJobsDto> recruiterJobs = jobPostActivityService.getRecruiterJobs(
                        ((RecruiterProfile) currentUserProfile).getUserAccountId()
                );
                model.addAttribute("jobPost", recruiterJobs);
            } else {
                List<JobSeekerApply> jobSeekerApplyList = jobSeekerApplyService.getCandidatesJobs(
                        (JobSeekerProfile) currentUserProfile
                );
                List<JobSeekerSave> jobSeekerSaveList = jobSeekerSaveService.getCandidatesJob(
                        (JobSeekerProfile) currentUserProfile
                );

                for (JobPostActivity jobPostActivity : jobPost) {
                    boolean exist = jobSeekerApplyList.stream()
                            .anyMatch(a -> Objects.equals(a.getJob().getJobPostId(), jobPostActivity.getJobPostId()));
                    boolean saved = jobSeekerSaveList.stream()
                            .anyMatch(s -> Objects.equals(s.getJob().getJobPostId(), jobPostActivity.getJobPostId()));

                    jobPostActivity.setIsActive(exist);
                    jobPostActivity.setIsSaved(saved);
                }

                model.addAttribute("jobPost", jobPost);
            }
        }

        model.addAttribute("user", currentUserProfile);
        return "dashboard";
    }


    @GetMapping("/dashboard/add")
    public String addJobs(Model model){
        model.addAttribute("jobPostActivity", new JobPostActivity());
        model.addAttribute("user",usersService.getCurrentUserProfile());
        return "add_jobs";
    }

    @PostMapping("/dashboard/addNew")
    public String addNewJob(JobPostActivity jobPostActivity, Model model){
        Users user=usersService.getCurrentUser();

        if(user!=null){
            jobPostActivity.setPostedById(user);
        }
        jobPostActivity.setPostedDate(LocalDateTime.now());
        model.addAttribute("jobPosActivity", jobPostActivity);

        jobPostActivityService.addNew(jobPostActivity);

        return "redirect:/dashboard/";
    }

    @PostMapping("/dashboard/edit/{id}")
    public String editJob(@PathVariable("id") int id, Model model){
        model.addAttribute("jobPostActivity",jobPostActivityService.getJobById(id));
        model.addAttribute("user",usersService.getCurrentUserProfile());
        return "add_jobs";
    }


}
