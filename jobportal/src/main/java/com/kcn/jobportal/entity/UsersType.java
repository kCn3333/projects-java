package com.kcn.jobportal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users_type")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class UsersType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userTypeId;

    @Column(name="user_type_name")
    private String userTypeName;

    @ManyToMany (targetEntity = Users.class, mappedBy="userTypeId", cascade = CascadeType.ALL)
    private List<Users> users;

    @Override
    public String toString() {
        return "UserType{" +
                "userTypeName='" + userTypeName + '\'' +
                ", userTypeId=" + userTypeId +
                '}';
    }
}
