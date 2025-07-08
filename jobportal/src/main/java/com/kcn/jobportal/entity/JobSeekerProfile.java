package com.kcn.jobportal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="job_seeker_profile")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class JobSeekerProfile {

    @Id
    private Integer userAccountId;

    @OneToOne
    @JoinColumn(name = "user_account_id")
    @MapsId
    private Users userId;

    private String city;

    @Column(name = "employment_type")
    private String employmentType;
    private String country;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "profile_photo", length = 64)
    private String profilePhoto;

    private String state;

    private String resume;

    @Column(name = "work_authorization")
    private String workAuthorization;

    @OneToMany(targetEntity = Skills.class, mappedBy = "jobSeekerProfile", cascade = CascadeType.ALL)
    private List<Skills> skills;

    public JobSeekerProfile(Users users) {
        this.userId=users;
    }

    @Transient
    public String getPhotosImagePath(){
        if(profilePhoto==null || userAccountId==null)
            return null;
        return "/photos/candidate/" + userAccountId + "/" + profilePhoto;

    }
}
