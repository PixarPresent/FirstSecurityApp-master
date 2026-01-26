package web.FirstSecurityApp.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.FirstSecurityApp.models.Like;
import web.FirstSecurityApp.models.Post;
import web.FirstSecurityApp.models.User;
import web.FirstSecurityApp.repositories.LikeRepository;
import web.FirstSecurityApp.repositories.PostRepository;
import web.FirstSecurityApp.repositories.UserRepository;
import web.FirstSecurityApp.services.LikeService;

import java.util.Set;

@Service
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
    @Transactional
    public boolean toggleLike(Long postId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found: " + postId));

        Like existingLike = likeRepository.findByUserAndPost(user, post).orElse(null);
        
        if (existingLike != null) {
            // Unlike
            likeRepository.delete(existingLike);
            return false;
        } else {
            // Like
            Like newLike = new Like();
            newLike.setUser(user);
            newLike.setPost(post);
            likeRepository.save(newLike);
            return true;
        }
    }

    @Override
    public int getLikeCount(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found: " + postId));
        return likeRepository.countByPost(post);
    }

    @Override
    public Set<User> getUsersWhoLikedPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found: " + postId));
        return likeRepository.findUsersByPost(post);
    }

    @Override
    public boolean isPostLikedByUser(Long postId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found: " + postId));

        return likeRepository.findByUserAndPost(user, post).isPresent();
    }
}
