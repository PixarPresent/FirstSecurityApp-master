package web.FirstSecurityApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import web.FirstSecurityApp.models.Media;
import web.FirstSecurityApp.models.Post;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
    
    List<Media> findByPost(Post post);
    
    List<Media> findByPostId(Long postId);
    
    Optional<Media> findByFileName(String fileName);
    
    @Query("SELECT m FROM Media m WHERE m.post.id = :postId ORDER BY m.createdAt ASC")
    List<Media> findByPostIdOrderByCreatedAt(@Param("postId") Long postId);
    
    void deleteByPostId(Long postId);
    
    boolean existsByFileName(String fileName);
}
