package web.FirstSecurityApp.mappers;

import web.FirstSecurityApp.dto.PostRequest;
import web.FirstSecurityApp.dto.PostResponse;
import web.FirstSecurityApp.dto.UserResponse;
import web.FirstSecurityApp.models.Post;
import web.FirstSecurityApp.models.User;

public class PostMapper {

    public static PostResponse toResponse(Post post, String currentUsername, long likeCount, boolean isLikedByCurrentUser) {
        UserResponse authorResponse = new UserResponse(
                post.getAuthor().getId(),
                post.getAuthor().getUsername(),
                post.getAuthor().getEmail(),
                post.getAuthor().getAvatarPath()
        );

        return new PostResponse(
                post.getId(),
                post.getContent(),
                authorResponse,
                likeCount,
                post.getCreatedAt(),
                post.getUpdatedAt(),
                isLikedByCurrentUser
        );
    }

    public static Post toEntity(PostRequest postRequest, User author) {
        return new Post(postRequest.getContent(), author);
    }
}
