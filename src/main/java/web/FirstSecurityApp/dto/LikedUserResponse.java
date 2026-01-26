package web.FirstSecurityApp.dto;

import java.time.LocalDateTime;

public class LikedUserResponse {
    private Long id;
    private String username;
    private String email;
    private String avatarPath;
    private LocalDateTime likedAt;

    public LikedUserResponse() {}

    public LikedUserResponse(Long id, String username, String email, String avatarPath, LocalDateTime likedAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.avatarPath = avatarPath;
        this.likedAt = likedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public LocalDateTime getLikedAt() {
        return likedAt;
    }

    public void setLikedAt(LocalDateTime likedAt) {
        this.likedAt = likedAt;
    }
}
