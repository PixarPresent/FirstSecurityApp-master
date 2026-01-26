package web.FirstSecurityApp.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import web.FirstSecurityApp.services.LikeService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<Map<String, Object>> toggleLike(
            @PathVariable Long postId,
            Authentication authentication) {
        
        String currentUsername = authentication.getName();
        boolean isLiked = likeService.toggleLike(postId, currentUsername);
        int likeCount = likeService.getLikeCount(postId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("liked", isLiked);
        response.put("likeCount", likeCount);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{postId}/likes")
    public ResponseEntity<Map<String, Object>> getLikeInfo(
            @PathVariable Long postId,
            Authentication authentication) {
        
        int likeCount = likeService.getLikeCount(postId);
        boolean isLiked = false;
        
        if (authentication != null) {
            String currentUsername = authentication.getName();
            isLiked = likeService.isPostLikedByUser(postId, currentUsername);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("likeCount", likeCount);
        response.put("isLiked", isLiked);
        
        return ResponseEntity.ok(response);
    }
}
