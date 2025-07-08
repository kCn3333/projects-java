package com.kcn.jobportal.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recruiter_profile")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RecruiterProfile {

    @Id
    private int userAccountId;

    @OneToOne
    @JoinColumn(name = "user_account_id")
    @MapsId
    private Users userId;

    private String city;
    private String company;
    private String country;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "profile_photo", length = 64)
    private String profilePhoto;

    private String state;

    @Transient
    public String getPhotosImagePath() {
        if (profilePhoto != null) {
            return "/photos/recruiter/" + userAccountId + "/" + profilePhoto;
        } else {
            return null;
        }
    }


    public RecruiterProfile(Users users) {
        this.userId=users;
    }
}
