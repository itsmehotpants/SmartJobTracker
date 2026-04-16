package com.jobtracker.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobStatsResponse {

    private long totalJobs;
    private long activeJobs;
    private long expiredJobs;
    private Map<String, Long> jobsByType;
    private Map<String, Long> jobsByExperience;
}
