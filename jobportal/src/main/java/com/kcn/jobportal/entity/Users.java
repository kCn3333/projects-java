package com.kcn.jobportal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Users {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name="user_id")
    private int userId;

    @Column(name = "email", unique = true)
    @NotEmpty
    private String email;

    @Column(name="is_active")
    private boolean isActive;

    @Column(name="password")
    @NotEmpty
    private String password;

    @Column(name="registration_date")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date registrationDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="userTypeId", referencedColumnName = "userTypeId")
    private UsersType userTypeId;



}
