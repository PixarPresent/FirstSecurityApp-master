package web.FirstSecurityApp.services;

import org.springframework.web.multipart.MultipartFile;
import web.FirstSecurityApp.models.Media;

import java.util.List;

public interface MediaService {
    
    Media saveMedia(MultipartFile file, Long postId);
    
    List<Media> saveMediaFiles(List<MultipartFile> files, Long postId);
    
    List<Media> getMediaByPostId(Long postId);
    
    void deleteMedia(Long mediaId);
    
    void deleteMediaByPostId(Long postId);
    
    Media getMediaById(Long mediaId);
    
    Media getMediaByFileName(String fileName);
    
    String generateUniqueFileName(String originalFileName);
    
    boolean isValidImageType(MultipartFile file);
    
    boolean isValidFileSize(MultipartFile file);
}
