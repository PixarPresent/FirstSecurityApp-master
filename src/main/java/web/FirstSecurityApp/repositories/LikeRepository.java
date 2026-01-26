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
import java.util.Set;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    
    Optional<Like> findByUserAndPost(User user, Post post);
    
    boolean existsByPostAndUser(Post post, User user);
    
    @Query("SELECT l.user FROM Like l WHERE l.post = :post")
    Set<User> findUsersByPost(@Param("post") Post post);
    
    @Query("SELECT COUNT(l) FROM Like l WHERE l.post = :post")
    int countByPost(@Param("post") Post post);
    
    @Modifying
    @Query("DELETE FROM Like l WHERE l.post = :post AND l.user = :user")
    void deleteByPostAndUser(@Param("post") Post post, @Param("user") User user);
}
