package com.jobtracker.demo.mapper;

import com.jobtracker.demo.dto.response.ApplicationResponse;
import com.jobtracker.demo.entity.Application;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMapper {

    private final JobMapper jobMapper;

    public ApplicationMapper(JobMapper jobMapper) {
        this.jobMapper = jobMapper;
    }

    public ApplicationResponse toResponse(Application application) {
        return ApplicationResponse.builder()
                .id(application.getId())
                .job(jobMapper.toResponse(application.getJob()))
                .status(application.getStatus())
                .notes(application.getNotes())
                .resumeLink(application.getResumeLink())
                .appliedDate(application.getAppliedDate())
                .lastUpdated(application.getLastUpdated())
                .build();
    }
}
