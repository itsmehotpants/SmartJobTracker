package com.jobtracker.demo.dto.response;

import com.jobtracker.demo.entity.enums.ExperienceLevel;
import com.jobtracker.demo.entity.enums.JobType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobResponse {

    private Long id;
    private String title;
    private String company;
    private String location;
    private String description;
    private String link;
    private String salary;
    private JobType jobType;
    private ExperienceLevel experienceLevel;
    private LocalDate deadline;
    private Boolean isActive;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
