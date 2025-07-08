package com.kcn.jobportal.entity;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
public class RecruiterJobsDto {

    private Long totalCandidates;
    private Integer jobPostId;
    private String JobTitle;
    private JobLocation jobLocationId;
    private JobCompany jobCompanyId;


}
