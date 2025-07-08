package com.kcn.jobportal.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "job_post_activity")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class JobPostActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer jobPostId;

    @ManyToOne
    @JoinColumn(name ="postedById", referencedColumnName = "userId")
    private Users postedById;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "jobLocationId", referencedColumnName = "Id")
    private JobLocation jobLocationId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name ="jobCompanyId", referencedColumnName ="Id")
    private JobCompany jobCompanyId;

    @Transient
    private boolean isActive;

    @Transient
    private boolean isSaved;

    @Length(max= 1000)
    private String descriptionOfJob;

    private String jobTitle;
    private String jobType;
    private String remote;
    private String salary;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime postedDate;

    public boolean getIsActive() {
        return isActive;
    }

    public boolean getIsSaved() {
        return isSaved;
    }
    public void setIsActive(boolean is) {
        this.isActive=is;
    }

    public void setIsSaved(boolean is) {
        this.isSaved=is;
    }
}
