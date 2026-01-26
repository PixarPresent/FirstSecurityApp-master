package web.FirstSecurityApp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import web.FirstSecurityApp.dto.PostRequest;
import web.FirstSecurityApp.dto.PostResponse;
import web.FirstSecurityApp.models.Post;

import java.util.List;

public interface PostService {
    
    Page<PostResponse> getAllPosts(Pageable pageable);
    
    Page<PostResponse> getMyPosts(Pageable pageable, String currentUsername);
    
    PostResponse createPost(PostRequest postRequest, String currentUsername);
    
    PostResponse createPostWithMedia(String content, MultipartFile[] files, String currentUsername);
    
    PostResponse updatePost(Long id, PostRequest postRequest, String currentUsername);
    
    void deletePost(Long id, String currentUsername);
    
    PostResponse getPostById(Long id, String currentUsername);
    
    PostResponse convertToResponse(Post post, String currentUsername);
    
    List<PostResponse> convertToResponseList(List<Post> posts, String currentUsername);
}
