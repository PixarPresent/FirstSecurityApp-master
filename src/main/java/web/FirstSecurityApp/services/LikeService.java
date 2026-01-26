package web.FirstSecurityApp.services;

import org.springframework.stereotype.Service;
import web.FirstSecurityApp.models.Post;
import web.FirstSecurityApp.models.User;

import java.util.Set;

@Service
public interface LikeService {
    boolean toggleLike(Long postId, String username);
    int getLikeCount(Long postId);
    Set<User> getUsersWhoLikedPost(Long postId);
    boolean isPostLikedByUser(Long postId, String username);
}
