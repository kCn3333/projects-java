package com.kcn.jobportal.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "skills")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Skills {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job_seeker_profile")
    private JobSeekerProfile jobSeekerProfile;

    private String name;

    @Column(name = "experience_level")
    private String experienceLevel;

    @Column(name = "years_of_experience")
    private String yearsOfExperience;


}
