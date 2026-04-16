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
public class ApplicationStatsResponse {

    private long totalApplications;
    private Map<String, Long> applicationsByStatus;
    private double interviewRate;
    private double offerRate;
}
