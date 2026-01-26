package web.FirstSecurityApp.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import web.FirstSecurityApp.models.Post;
import web.FirstSecurityApp.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    List<Post> findByAuthor(User author);
    
    Page<Post> findByAuthor(User author, Pageable pageable);
    
    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.author LEFT JOIN FETCH p.likes ORDER BY p.createdAt DESC")
    List<Post> findAllWithAuthorAndLikes();
    
    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.author LEFT JOIN FETCH p.likes ORDER BY p.createdAt DESC")
    Page<Post> findAllWithAuthorAndLikes(Pageable pageable);
    
    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.author LEFT JOIN FETCH p.likes WHERE p.id = :id")
    Optional<Post> findByIdWithAuthorAndLikes(@Param("id") Long id);
    
    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.author LEFT JOIN FETCH p.likes WHERE p.author.id = :authorId ORDER BY p.createdAt DESC")
    List<Post> findByAuthorIdWithLikes(@Param("authorId") Long authorId);
    
    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.author LEFT JOIN FETCH p.likes WHERE p.author.id = :authorId ORDER BY p.createdAt DESC")
    Page<Post> findByAuthorIdWithLikes(@Param("authorId") Long authorId, Pageable pageable);
    
    @Query("SELECT COUNT(p) FROM Post p WHERE p.author = :author")
    long countByAuthor(@Param("author") User author);
    
    @Query("SELECT p FROM Post p WHERE p.content LIKE %:keyword% ORDER BY p.createdAt DESC")
    Page<Post> findByContentContaining(@Param("keyword") String keyword, Pageable pageable);
}
