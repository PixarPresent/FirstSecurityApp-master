package web.FirstSecurityApp.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import web.FirstSecurityApp.models.Like;
import web.FirstSecurityApp.models.Post;
import web.FirstSecurityApp.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    
    Optional<Like> findByPostAndUser(Post post, User user);
    
    boolean existsByPostAndUser(Post post, User user);
    
    List<Like> findByPost(Post post);
    
    Page<Like> findByPost(Post post, Pageable pageable);
    
    List<Like> findByUser(User user);
    
    Page<Like> findByUser(User user, Pageable pageable);
    
    @Query("SELECT COUNT(l) FROM Like l WHERE l.post = :post")
    long countByPost(@Param("post") Post post);
    
    @Query("SELECT COUNT(l) FROM Like l WHERE l.user = :user")
    long countByUser(@Param("user") User user);
    
    @Modifying
    @Query("DELETE FROM Like l WHERE l.post = :post AND l.user = :user")
    void deleteByPostAndUser(@Param("post") Post post, @Param("user") User user);
    
    @Query("SELECT l FROM Like l LEFT JOIN FETCH l.post LEFT JOIN FETCH l.user WHERE l.post.id = :postId")
    List<Like> findByPostIdWithUser(@Param("postId") Long postId);
    
    @Query("SELECT l FROM Like l LEFT JOIN FETCH l.post LEFT JOIN FETCH l.user WHERE l.user.id = :userId")
    List<Like> findByUserIdWithPost(@Param("userId") Long userId);
    
    @Query("SELECT l FROM Like l LEFT JOIN FETCH l.post LEFT JOIN FETCH l.user WHERE l.post.id = :postId AND l.user.id = :userId")
    Optional<Like> findByPostIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);
}
