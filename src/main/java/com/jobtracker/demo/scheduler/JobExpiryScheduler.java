package com.jobtracker.demo.scheduler;

import com.jobtracker.demo.entity.Job;
import com.jobtracker.demo.repository.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
public class JobExpiryScheduler {

    private static final Logger logger = LoggerFactory.getLogger(JobExpiryScheduler.class);

    private final JobRepository jobRepository;

    public JobExpiryScheduler(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    /**
     * Runs daily at midnight to deactivate expired jobs.
     * Jobs with a deadline in the past are automatically marked as inactive.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void deactivateExpiredJobs() {
        logger.info("[SCHEDULER] Running job expiry check...");

        List<Job> expiredJobs = jobRepository.findByDeadlineBeforeAndIsActiveTrue(LocalDate.now());

        if (expiredJobs.isEmpty()) {
            logger.info("[SCHEDULER] No expired jobs found.");
            return;
        }

        expiredJobs.forEach(job -> {
            job.setIsActive(false);
            logger.debug("[SCHEDULER] Deactivating job: {} (ID: {})", job.getTitle(), job.getId());
        });

        jobRepository.saveAll(expiredJobs);
        logger.info("[SCHEDULER] Deactivated {} expired jobs.", expiredJobs.size());
    }
}
