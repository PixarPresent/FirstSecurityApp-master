package web.FirstSecurityApp.dto;

public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String avatarPath;

    public UserResponse() {}

    public UserResponse(Long id, String username, String email, String avatarPath) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.avatarPath = avatarPath;
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
}
