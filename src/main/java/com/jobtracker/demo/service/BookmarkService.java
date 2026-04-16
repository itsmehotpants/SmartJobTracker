package com.jobtracker.demo.service;

import com.jobtracker.demo.dto.request.BookmarkRequest;
import com.jobtracker.demo.dto.response.BookmarkResponse;
import com.jobtracker.demo.dto.response.PagedResponse;
import com.jobtracker.demo.entity.Bookmark;
import com.jobtracker.demo.entity.Job;
import com.jobtracker.demo.entity.User;
import com.jobtracker.demo.exception.ResourceNotFoundException;
import com.jobtracker.demo.mapper.BookmarkMapper;
import com.jobtracker.demo.repository.BookmarkRepository;
import com.jobtracker.demo.repository.JobRepository;
import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.StringWriter;
import java.util.List;
import java.util.Optional;

@Service
public class BookmarkService {

    private static final Logger logger = LoggerFactory.getLogger(BookmarkService.class);

    private final BookmarkRepository bookmarkRepository;
    private final JobRepository jobRepository;
    private final BookmarkMapper bookmarkMapper;

    public BookmarkService(BookmarkRepository bookmarkRepository,
                           JobRepository jobRepository,
                           BookmarkMapper bookmarkMapper) {
        this.bookmarkRepository = bookmarkRepository;
        this.jobRepository = jobRepository;
        this.bookmarkMapper = bookmarkMapper;
    }

    @Transactional
    public BookmarkResponse toggleBookmark(BookmarkRequest request, User user) {
        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + request.getJobId()));

        Optional<Bookmark> existing = bookmarkRepository.findByUserIdAndJobId(user.getId(), job.getId());

        if (existing.isPresent()) {
            bookmarkRepository.delete(existing.get());
            logger.info("Bookmark removed: user={} job={}", user.getEmail(), job.getTitle());
            return null; // indicates removal
        }

        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .job(job)
                .notes(request.getNotes())
                .build();

        Bookmark saved = bookmarkRepository.save(bookmark);
        logger.info("Bookmark added: user={} job={}", user.getEmail(), job.getTitle());
        return bookmarkMapper.toResponse(saved);
    }

    public PagedResponse<BookmarkResponse> getUserBookmarks(Long userId, Pageable pageable) {
        Page<Bookmark> page = bookmarkRepository.findByUserId(userId, pageable);
        return PagedResponse.<BookmarkResponse>builder()
                .content(page.getContent().stream().map(bookmarkMapper::toResponse).toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    @Transactional
    public void deleteBookmark(Long bookmarkId, Long userId) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new ResourceNotFoundException("Bookmark not found with id: " + bookmarkId));

        if (!bookmark.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Bookmark not found with id: " + bookmarkId);
        }

        bookmarkRepository.delete(bookmark);
        logger.info("Bookmark deleted: id={}", bookmarkId);
    }

    public String exportBookmarksCsv(Long userId) {
        List<Bookmark> bookmarks = bookmarkRepository.findByUserId(userId);

        StringWriter stringWriter = new StringWriter();
        try (CSVWriter csvWriter = new CSVWriter(stringWriter)) {
            // Header
            csvWriter.writeNext(new String[]{
                    "Bookmark ID", "Job Title", "Company", "Location",
                    "Job Type", "Experience Level", "Salary", "Deadline",
                    "Link", "Notes", "Bookmarked At"
            });

            // Data rows
            for (Bookmark b : bookmarks) {
                Job job = b.getJob();
                csvWriter.writeNext(new String[]{
                        String.valueOf(b.getId()),
                        job.getTitle(),
                        job.getCompany(),
                        job.getLocation(),
                        job.getJobType().name(),
                        job.getExperienceLevel().name(),
                        job.getSalary() != null ? job.getSalary() : "N/A",
                        job.getDeadline() != null ? job.getDeadline().toString() : "N/A",
                        job.getLink() != null ? job.getLink() : "N/A",
                        b.getNotes() != null ? b.getNotes() : "",
                        b.getCreatedAt().toString()
                });
            }
        } catch (Exception e) {
            logger.error("Error exporting bookmarks to CSV", e);
            throw new RuntimeException("Failed to export bookmarks", e);
        }

        return stringWriter.toString();
    }

    public long countByUserId(Long userId) {
        return bookmarkRepository.countByUserId(userId);
    }
}
