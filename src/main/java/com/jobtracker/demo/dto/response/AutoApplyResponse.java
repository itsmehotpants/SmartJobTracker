package com.jobtracker.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutoApplyResponse {
    
    private ApplicationResponse application;
    private int matchScore;
    private String analysisSummary;
    private List<String> missingKeywords;
    private List<String> matchedKeywords;
}
