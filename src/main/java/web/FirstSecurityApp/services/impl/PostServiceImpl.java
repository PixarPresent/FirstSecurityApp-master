package web.FirstSecurityApp.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import web.FirstSecurityApp.dto.MediaResponse;
import web.FirstSecurityApp.dto.PostRequest;
import web.FirstSecurityApp.dto.PostResponse;
import web.FirstSecurityApp.dto.UserResponse;
import web.FirstSecurityApp.exceptions.UserIncorrectData;
import web.FirstSecurityApp.models.Like;
import web.FirstSecurityApp.models.Media;
import web.FirstSecurityApp.models.Post;
import web.FirstSecurityApp.models.User;
import web.FirstSecurityApp.repositories.LikeRepository;
import web.FirstSecurityApp.repositories.MediaRepository;
import web.FirstSecurityApp.repositories.PostRepository;
import web.FirstSecurityApp.repositories.UserRepository;
import web.FirstSecurityApp.services.MediaService;
import web.FirstSecurityApp.services.PostService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final MediaService mediaService;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, 
                          LikeRepository likeRepository, MediaService mediaService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
        this.mediaService = mediaService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostResponse> getAllPosts(Pageable pageable) {
        String currentUsername = getCurrentUsername();
        Page<Post> posts = postRepository.findAllWithAuthorAndLikes(pageable);
        return posts.map(post -> convertToResponse(post, currentUsername));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostResponse> getMyPosts(Pageable pageable, String currentUsername) {
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UserIncorrectData("User not found"));
        
        Page<Post> posts = postRepository.findByAuthorIdWithLikes(currentUser.getId(), pageable);
        return posts.map(post -> convertToResponse(post, currentUsername));
    }

    @Override
    public PostResponse createPost(PostRequest postRequest, String currentUsername) {
        User author = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UserIncorrectData("User not found"));

        Post post = new Post(postRequest.getContent(), author);
        Post savedPost = postRepository.save(post);
        
        return convertToResponse(savedPost, currentUsername);
    }

    @Override
    public PostResponse createPostWithMedia(String content, MultipartFile[] files, String currentUsername) {
        User author = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UserIncorrectData("User not found"));

        Post post = new Post(content, author);
        Post savedPost = postRepository.save(post);
        
        if (files != null && files.length > 0) {
            List<MultipartFile> fileList = List.of(files);
            mediaService.saveMediaFiles(fileList, savedPost.getId());
        }
        
        return convertToResponse(savedPost, currentUsername);
    }

    @Override
    public PostResponse updatePost(Long id, PostRequest postRequest, String currentUsername) {
        Post post = postRepository.findByIdWithAuthorAndLikes(id)
                .orElseThrow(() -> new UserIncorrectData("Post not found"));

        if (!post.getAuthor().getUsername().equals(currentUsername)) {
            throw new UserIncorrectData("You can only edit your own posts");
        }

        post.setContent(postRequest.getContent());
        Post updatedPost = postRepository.save(post);
        
        return convertToResponse(updatedPost, currentUsername);
    }

    @Override
    public void deletePost(Long id, String currentUsername) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new UserIncorrectData("Post not found"));

        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UserIncorrectData("User not found"));

        boolean isAuthor = post.getAuthor().getUsername().equals(currentUsername);
        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));

        if (!isAuthor && !isAdmin) {
            throw new UserIncorrectData("You can only delete your own posts");
        }

        mediaService.deleteMediaByPostId(id);
        postRepository.delete(post);
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponse getPostById(Long id, String currentUsername) {
        Post post = postRepository.findByIdWithAuthorAndLikes(id)
                .orElseThrow(() -> new UserIncorrectData("Post not found"));
        
        return convertToResponse(post, currentUsername);
    }

    @Override
    public PostResponse convertToResponse(Post post, String currentUsername) {
        UserResponse authorResponse = new UserResponse(
                post.getAuthor().getId(),
                post.getAuthor().getUsername(),
                post.getAuthor().getEmail(),
                post.getAuthor().getAvatarPath()
        );

        long likeCount = likeRepository.countByPost(post);
        boolean isLikedByCurrentUser = false;
        
        if (currentUsername != null) {
            User currentUser = userRepository.findByUsername(currentUsername).orElse(null);
            if (currentUser != null) {
                isLikedByCurrentUser = likeRepository.existsByPostAndUser(post, currentUser);
            }
        }

        List<MediaResponse> mediaResponses = new ArrayList<>();
        try {
            if (post.getMediaFiles() != null) {
                mediaResponses = post.getMediaFiles().stream()
                        .map(this::convertMediaToResponse)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            // Handle lazy loading exception
            mediaResponses = mediaService.getMediaByPostId(post.getId()).stream()
                    .map(this::convertMediaToResponse)
                    .collect(Collectors.toList());
        }

        return new PostResponse(
                post.getId(),
                post.getContent(),
                authorResponse,
                likeCount,
                post.getCreatedAt(),
                post.getUpdatedAt(),
                isLikedByCurrentUser,
                mediaResponses
        );
    }

    private MediaResponse convertMediaToResponse(Media media) {
        String fileUrl = "/api/media/" + media.getFileName();
        return new MediaResponse(
                media.getId(),
                media.getFileName(),
                media.getOriginalFileName(),
                media.getContentType(),
                media.getFileSize(),
                fileUrl,
                media.getCreatedAt()
        );
    }

    @Override
    public List<PostResponse> convertToResponseList(List<Post> posts, String currentUsername) {
        return posts.stream()
                .map(post -> convertToResponse(post, currentUsername))
                .collect(Collectors.toList());
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : null;
    }
}
