package com.kcn.jobportal.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "job_location")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class JobLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    private String city;
    private String country;
    private String state;
}
