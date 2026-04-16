package com.jobtracker.demo.controller;

import com.jobtracker.demo.dto.request.BookmarkRequest;
import com.jobtracker.demo.dto.response.ApiResponse;
import com.jobtracker.demo.dto.response.BookmarkResponse;
import com.jobtracker.demo.dto.response.PagedResponse;
import com.jobtracker.demo.entity.User;
import com.jobtracker.demo.service.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookmarks")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Bookmarks", description = "Save and manage bookmarked jobs")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @PostMapping("/toggle")
    @Operation(summary = "Toggle bookmark", description = "If bookmarked → removes it. If not → adds it. Idempotent design.")
    public ResponseEntity<ApiResponse<BookmarkResponse>> toggleBookmark(
            @Valid @RequestBody BookmarkRequest request,
            @AuthenticationPrincipal User user) {

        BookmarkResponse response = bookmarkService.toggleBookmark(request, user);
        if (response == null) {
            return ResponseEntity.ok(ApiResponse.success("Bookmark removed successfully"));
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Bookmark added successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get my bookmarks", description = "Returns paginated list of current user's bookmarks")
    public ResponseEntity<ApiResponse<PagedResponse<BookmarkResponse>>> getMyBookmarks(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        PagedResponse<BookmarkResponse> response = bookmarkService.getUserBookmarks(user.getId(), pageable);
        return ResponseEntity.ok(ApiResponse.success("Bookmarks retrieved successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a bookmark")
    public ResponseEntity<ApiResponse<Void>> deleteBookmark(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {

        bookmarkService.deleteBookmark(id, user.getId());
        return ResponseEntity.ok(ApiResponse.success("Bookmark deleted successfully"));
    }

    @GetMapping("/export")
    @Operation(summary = "Export bookmarks as CSV", description = "Downloads all bookmarks as a CSV file")
    public ResponseEntity<byte[]> exportBookmarksCsv(@AuthenticationPrincipal User user) {
        String csv = bookmarkService.exportBookmarksCsv(user.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "bookmarks_" + user.getId() + ".csv");

        return ResponseEntity.ok()
                .headers(headers)
                .body(csv.getBytes());
    }
}
