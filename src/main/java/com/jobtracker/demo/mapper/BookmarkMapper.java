package com.jobtracker.demo.mapper;

import com.jobtracker.demo.dto.response.BookmarkResponse;
import com.jobtracker.demo.entity.Bookmark;
import org.springframework.stereotype.Component;

@Component
public class BookmarkMapper {

    private final JobMapper jobMapper;

    public BookmarkMapper(JobMapper jobMapper) {
        this.jobMapper = jobMapper;
    }

    public BookmarkResponse toResponse(Bookmark bookmark) {
        return BookmarkResponse.builder()
                .id(bookmark.getId())
                .job(jobMapper.toResponse(bookmark.getJob()))
                .notes(bookmark.getNotes())
                .createdAt(bookmark.getCreatedAt())
                .build();
    }
}
