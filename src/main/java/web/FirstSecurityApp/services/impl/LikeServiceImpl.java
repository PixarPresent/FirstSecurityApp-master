package web.FirstSecurityApp.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.FirstSecurityApp.dto.LikeResponse;
import web.FirstSecurityApp.dto.LikedUserResponse;
import web.FirstSecurityApp.exceptions.PostNotFoundException;
import web.FirstSecurityApp.exceptions.UserIncorrectData;
import web.FirstSecurityApp.models.Like;
import web.FirstSecurityApp.models.Post;
import web.FirstSecurityApp.models.User;
import web.FirstSecurityApp.repositories.LikeRepository;
import web.FirstSecurityApp.repositories.PostRepository;
import web.FirstSecurityApp.repositories.UserRepository;
import web.FirstSecurityApp.services.LikeService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public LikeServiceImpl(LikeRepository likeRepository, PostRepository postRepository, UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public LikeResponse toggleLike(Long postId, String currentUsername) {
        // Validate post exists
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));

        // Get current user
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UserIncorrectData("User not found: " + currentUsername));

        // Check if like already exists (thread-safe check)
        boolean isLiked = likeRepository.existsByPostAndUser(post, currentUser);
        
        if (isLiked) {
            // Remove like
            likeRepository.deleteByPostAndUser(post, currentUser);
        } else {
            // Add like
            Like like = new Like(post, currentUser);
            likeRepository.save(like);
        }

        // Get updated like count
        long likeCount = likeRepository.countByPost(post);
        
        return new LikeResponse(!isLiked, (int) likeCount);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isPostLikedByUser(Long postId, String currentUsername) {
        if (currentUsername == null) {
            return false;
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));

        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UserIncorrectData("User not found: " + currentUsername));

        return likeRepository.existsByPostAndUser(post, currentUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LikedUserResponse> getUsersWhoLikedPost(Long postId, Pageable pageable) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));

        List<Like> likes = likeRepository.findByPost(post);
        
        return likes.stream()
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .map(this::convertToLikedUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LikedUserResponse> getUsersWhoLikedPostPaginated(Long postId, Pageable pageable) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));

        Page<Like> likesPage = likeRepository.findByPost(post, pageable);
        
        return likesPage.map(this::convertToLikedUserResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public long getLikeCount(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));

        return likeRepository.countByPost(post);
    }

    private LikedUserResponse convertToLikedUserResponse(Like like) {
        User user = like.getUser();
        return new LikedUserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getAvatarPath(),
                like.getLikedAt()
        );
    }
}
