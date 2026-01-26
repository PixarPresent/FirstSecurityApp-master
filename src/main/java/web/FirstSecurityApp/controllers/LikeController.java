package web.FirstSecurityApp.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import web.FirstSecurityApp.dto.LikeResponse;
import web.FirstSecurityApp.dto.LikedUserResponse;
import web.FirstSecurityApp.services.LikeService;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<LikeResponse> toggleLike(
            @PathVariable Long postId,
            Authentication authentication) {
        
        String currentUsername = authentication.getName();
        LikeResponse response = likeService.toggleLike(postId, currentUsername);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{postId}/likes")
    public ResponseEntity<List<LikedUserResponse>> getUsersWhoLikedPost(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "likedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        List<LikedUserResponse> users = likeService.getUsersWhoLikedPost(postId, pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{postId}/likes/paginated")
    public ResponseEntity<Page<LikedUserResponse>> getUsersWhoLikedPostPaginated(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "likedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<LikedUserResponse> usersPage = likeService.getUsersWhoLikedPostPaginated(postId, pageable);
        return ResponseEntity.ok(usersPage);
    }

    @GetMapping("/{postId}/liked")
    public ResponseEntity<Boolean> isPostLikedByUser(
            @PathVariable Long postId,
            Authentication authentication) {
        
        String currentUsername = authentication != null ? authentication.getName() : null;
        boolean isLiked = likeService.isPostLikedByUser(postId, currentUsername);
        return ResponseEntity.ok(isLiked);
    }

    @GetMapping("/{postId}/like-count")
    public ResponseEntity<Long> getLikeCount(@PathVariable Long postId) {
        long likeCount = likeService.getLikeCount(postId);
        return ResponseEntity.ok(likeCount);
    }
}
