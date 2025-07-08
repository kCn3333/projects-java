package com.kcn.jobportal.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "job_company")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class JobCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    private String logo;
    private String name;

}
