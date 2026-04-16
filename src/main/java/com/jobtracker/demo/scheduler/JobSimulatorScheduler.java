package com.jobtracker.demo.scheduler;

import com.jobtracker.demo.entity.Job;
import com.jobtracker.demo.entity.enums.ExperienceLevel;
import com.jobtracker.demo.entity.enums.JobType;
import com.jobtracker.demo.repository.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class JobSimulatorScheduler {

    private static final Logger logger = LoggerFactory.getLogger(JobSimulatorScheduler.class);
    private final JobRepository jobRepository;

    private int counter = 1;

    public JobSimulatorScheduler(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    // Runs every 5 minutes for demo purposes (in production, maybe every few hours)
    @Scheduled(cron = "0 0/5 * * * ?")
    public void generateLiveJobs() {
        logger.info("Simulating live job market: generating new internship listing...");

        Job liveJob = Job.builder()
                .title("Software Engineering Intern - Live Update " + counter)
                .company(counter % 2 == 0 ? "TechNova Startup" : "GlobalTech Corp")
                .location("Remote, World")
                .description("This is a live generated job from the market simulator. We are looking for an ambitious intern with skills in Java, React, and general full stack development. Join us to learn and grow!")
                .link("https://example.com/apply/live-intern-" + counter)
                .salary("Stipend: 50,000 INR/month")
                .jobType(JobType.INTERNSHIP)
                .experienceLevel(ExperienceLevel.ENTRY)
                .deadline(LocalDate.now().plusMonths(1))
                .isActive(true)
                .createdBy("admin@jobtracker.com")
                .build();

        jobRepository.save(liveJob);
        counter++;
        logger.info("Live job published: {}", liveJob.getTitle());
    }
}
