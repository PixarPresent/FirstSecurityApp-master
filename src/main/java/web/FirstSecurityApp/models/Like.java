package web.FirstSecurityApp.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "likes", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"post_id", "user_id"}, 
                                            name = "uk_post_user_like"))
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "liked_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime likedAt;

    public Like() {}

    public Like(Post post, User user) {
        this.post = post;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getLikedAt() {
        return likedAt;
    }

    public void setLikedAt(LocalDateTime likedAt) {
        this.likedAt = likedAt;
    }

    @Override
    public String toString() {
        return "Like{" +
                "id=" + id +
                ", postId=" + (post != null ? post.getId() : null) +
                ", userId=" + (user != null ? user.getId() : null) +
                ", likedAt=" + likedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Like like = (Like) o;
        
        if (post != null ? !post.equals(like.post) : like.post != null) return false;
        return user != null ? user.equals(like.user) : like.user == null;
    }

    @Override
    public int hashCode() {
        int result = post != null ? post.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }
}
