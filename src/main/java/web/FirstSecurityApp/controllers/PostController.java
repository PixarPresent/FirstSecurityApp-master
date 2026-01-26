package web.FirstSecurityApp.controllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.FirstSecurityApp.dto.PostRequest;
import web.FirstSecurityApp.dto.PostResponse;
import web.FirstSecurityApp.services.MediaService;
import web.FirstSecurityApp.services.PostService;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*")
public class PostController {

    private final PostService postService;
    private final MediaService mediaService;

    public PostController(PostService postService, MediaService mediaService) {
        this.postService = postService;
        this.mediaService = mediaService;
    }

    @GetMapping
    public ResponseEntity<Page<PostResponse>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<PostResponse> posts = postService.getAllPosts(pageable);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/my")
    public ResponseEntity<Page<PostResponse>> getMyPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            Authentication authentication) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        String currentUsername = authentication.getName();
        Page<PostResponse> posts = postService.getMyPosts(pageable, currentUsername);
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @Valid @RequestBody PostRequest postRequest,
            Authentication authentication) {
        
        String currentUsername = authentication.getName();
        PostResponse createdPost = postService.createPost(postRequest, currentUsername);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    @PostMapping(value = "/with-media", consumes = "multipart/form-data")
    public ResponseEntity<PostResponse> createPostWithMedia(
            @RequestPart("content") String content,
            @RequestPart(value = "files", required = false) MultipartFile[] files,
            Authentication authentication) {
        
        String currentUsername = authentication.getName();
        PostResponse createdPost = postService.createPostWithMedia(content, files, currentUsername);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostRequest postRequest,
            Authentication authentication) {
        
        String currentUsername = authentication.getName();
        PostResponse updatedPost = postService.updatePost(id, postRequest, currentUsername);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long id,
            Authentication authentication) {
        
        String currentUsername = authentication.getName();
        postService.deletePost(id, currentUsername);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(
            @PathVariable Long id,
            Authentication authentication) {
        
        String currentUsername = authentication != null ? authentication.getName() : null;
        PostResponse post = postService.getPostById(id, currentUsername);
        return ResponseEntity.ok(post);
    }
}
