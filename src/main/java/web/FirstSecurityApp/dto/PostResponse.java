package web.FirstSecurityApp.dto;

import web.FirstSecurityApp.models.User;

import java.time.LocalDateTime;
import java.util.List;

public class PostResponse {
    private Long id;
    private String content;
    private UserResponse author;
    private long likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isLikedByCurrentUser;
    private List<MediaResponse> mediaFiles;

    public PostResponse() {}

    public PostResponse(Long id, String content, UserResponse author, long likeCount, 
                       LocalDateTime createdAt, LocalDateTime updatedAt, boolean isLikedByCurrentUser, 
                       List<MediaResponse> mediaFiles) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.likeCount = likeCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isLikedByCurrentUser = isLikedByCurrentUser;
        this.mediaFiles = mediaFiles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserResponse getAuthor() {
        return author;
    }

    public void setAuthor(UserResponse author) {
        this.author = author;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isLikedByCurrentUser() {
        return isLikedByCurrentUser;
    }

    public void setLikedByCurrentUser(boolean likedByCurrentUser) {
        isLikedByCurrentUser = likedByCurrentUser;
    }

    public List<MediaResponse> getMediaFiles() {
        return mediaFiles;
    }

    public void setMediaFiles(List<MediaResponse> mediaFiles) {
        this.mediaFiles = mediaFiles;
    }
}
