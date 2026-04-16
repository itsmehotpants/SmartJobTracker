package com.jobtracker.demo.dto.response;

import com.jobtracker.demo.entity.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponse {

    private Long id;
    private JobResponse job;
    private ApplicationStatus status;
    private String notes;
    private String resumeLink;
    private LocalDateTime appliedDate;
    private LocalDateTime lastUpdated;
}
