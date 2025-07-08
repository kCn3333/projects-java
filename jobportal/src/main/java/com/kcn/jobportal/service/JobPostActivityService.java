package com.kcn.jobportal.service;

import com.kcn.jobportal.entity.*;
import com.kcn.jobportal.repository.JobPostActivityRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class JobPostActivityService {

    private final JobPostActivityRepository jobPostActivityRepository;

    public JobPostActivityService(JobPostActivityRepository jobPostActivityRepository) {
        this.jobPostActivityRepository = jobPostActivityRepository;
    }

    public JobPostActivity addNew(JobPostActivity jobPostActivity){
        return jobPostActivityRepository.save(jobPostActivity);
    }

    public List<RecruiterJobsDto> getRecruiterJobs(int recruiterId){
        List<IRecruiterJobs> recruiterJobs=jobPostActivityRepository.getRecruiterJobs(recruiterId);
        List<RecruiterJobsDto> recruiterJobsDtoList= new ArrayList<>();

        for(IRecruiterJobs recruiterJob: recruiterJobs){
            JobLocation location= new JobLocation(recruiterJob.getLocationId(), recruiterJob.getCity(), recruiterJob.getCountry(), recruiterJob.getState());
            JobCompany company=new JobCompany(recruiterJob.getCompanyId(), "", recruiterJob.getName());
            recruiterJobsDtoList.add(new RecruiterJobsDto(recruiterJob.getTotalCandidates(),recruiterJob.getJobPostId(),recruiterJob.getJobTitle(),location,company));
        }
        return recruiterJobsDtoList;
    }

    public JobPostActivity getJobById(int id) {
        return jobPostActivityRepository.findById(id).orElseThrow(()->new RuntimeException("Job not found"));
    }

    public List<JobPostActivity> getAll() {
        return jobPostActivityRepository.findAll();
    }

    public List<JobPostActivity> search(String job, String location, List<String> types, List<String> remotes, LocalDate searchDate) {
        // Convert empty strings to null
        String jobParam = StringUtils.hasText(job) ? job : null;
        String locationParam = StringUtils.hasText(location) ? location : null;

        // Convert lists to arrays - never pass null arrays
        String[] typeArray = (types == null || types.isEmpty())
                ? new String[]{}
                : types.toArray(new String[0]);
        String[] remoteArray = (remotes == null || remotes.isEmpty())
                ? new String[]{}
                : remotes.toArray(new String[0]);





        if (searchDate == null) {
            return jobPostActivityRepository.searchWithoutDate(
                    StringUtils.hasText(job) ? job : null,
                    StringUtils.hasText(location) ? location : null,
                    remoteArray,
                    typeArray
            );
        }



        java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(searchDate.atStartOfDay());

        System.out.println("Search Parameters:");
        System.out.println("Job: {} "+ job);
        System.out.println("Location: {} "+ location);
        System.out.println("Types: {} "+ Arrays.toString(typeArray));
        System.out.println("Remotes: {} "+ Arrays.toString(remoteArray));
        System.out.println("Date: {} "+ searchDate);

        return jobPostActivityRepository.search(
                StringUtils.hasText(job) ? job : null,
                StringUtils.hasText(location) ? location : null,
                remoteArray,
                typeArray,
                searchDate
        );
    }
}
