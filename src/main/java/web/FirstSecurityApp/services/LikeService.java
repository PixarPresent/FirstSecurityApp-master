package web.FirstSecurityApp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import web.FirstSecurityApp.dto.LikeResponse;
import web.FirstSecurityApp.dto.LikedUserResponse;

import java.util.List;

public interface LikeService {
    
    LikeResponse toggleLike(Long postId, String currentUsername);
    
    boolean isPostLikedByUser(Long postId, String currentUsername);
    
    List<LikedUserResponse> getUsersWhoLikedPost(Long postId, Pageable pageable);
    
    Page<LikedUserResponse> getUsersWhoLikedPostPaginated(Long postId, Pageable pageable);
    
    long getLikeCount(Long postId);
}
