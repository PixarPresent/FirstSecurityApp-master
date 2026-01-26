package web.FirstSecurityApp.services.impl;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import web.FirstSecurityApp.exceptions.UserIncorrectData;
import web.FirstSecurityApp.models.Media;
import web.FirstSecurityApp.models.Post;
import web.FirstSecurityApp.repositories.MediaRepository;
import web.FirstSecurityApp.repositories.PostRepository;
import web.FirstSecurityApp.services.MediaService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;
    private final PostRepository postRepository;

    @Value("${app.media.upload-dir:uploads/media}")
    private String uploadDir;

    @Value("${app.media.max-file-size:10485760}")
    private long maxFileSize;

    private static final List<String> ALLOWED_CONTENT_TYPES = List.of(
            "image/jpeg",
            "image/jpg",
            "image/png",
            "image/gif",
            "image/webp"
    );

    public MediaServiceImpl(MediaRepository mediaRepository, PostRepository postRepository) {
        this.mediaRepository = mediaRepository;
        this.postRepository = postRepository;
    }

    @PostConstruct
    private void init() {
        createUploadDirectoryIfNotExists();
    }

    @Override
    public Media saveMedia(MultipartFile file, Long postId) {
        if (file.isEmpty()) {
            throw new UserIncorrectData("File cannot be empty");
        }

        if (!isValidImageType(file)) {
            throw new UserIncorrectData("Invalid file type. Only JPEG, PNG, GIF, and WebP images are allowed");
        }

        if (!isValidFileSize(file)) {
            throw new UserIncorrectData("File size exceeds maximum allowed size of " + maxFileSize + " bytes");
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new UserIncorrectData("Post not found"));

        try {
            String uniqueFileName = generateUniqueFileName(file.getOriginalFilename());
            Path filePath = Paths.get(uploadDir, uniqueFileName);
            
            Files.createDirectories(filePath.getParent());
            Files.copy(file.getInputStream(), filePath);

            Media media = new Media(
                    uniqueFileName,
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getSize(),
                    filePath.toString(),
                    post
            );

            return mediaRepository.save(media);
        } catch (IOException e) {
            throw new UserIncorrectData("Failed to save file: " + e.getMessage());
        }
    }

    @Override
    public List<Media> saveMediaFiles(List<MultipartFile> files, Long postId) {
        List<Media> savedMedia = new ArrayList<>();
        
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                savedMedia.add(saveMedia(file, postId));
            }
        }
        
        return savedMedia;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Media> getMediaByPostId(Long postId) {
        return mediaRepository.findByPostIdOrderByCreatedAt(postId);
    }

    @Override
    public void deleteMedia(Long mediaId) {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new UserIncorrectData("Media not found"));

        try {
            Path filePath = Paths.get(media.getFilePath());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new UserIncorrectData("Failed to delete file: " + e.getMessage());
        }

        mediaRepository.delete(media);
    }

    @Override
    public void deleteMediaByPostId(Long postId) {
        List<Media> mediaFiles = mediaRepository.findByPostId(postId);
        
        for (Media media : mediaFiles) {
            try {
                Path filePath = Paths.get(media.getFilePath());
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                throw new UserIncorrectData("Failed to delete file: " + e.getMessage());
            }
        }
        
        mediaRepository.deleteByPostId(postId);
    }

    @Override
    @Transactional(readOnly = true)
    public Media getMediaById(Long mediaId) {
        return mediaRepository.findById(mediaId)
                .orElseThrow(() -> new UserIncorrectData("Media not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Media getMediaByFileName(String fileName) {
        return mediaRepository.findByFileName(fileName)
                .orElse(null);
    }

    @Override
    public String generateUniqueFileName(String originalFileName) {
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        return UUID.randomUUID().toString() + fileExtension;
    }

    @Override
    public boolean isValidImageType(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && ALLOWED_CONTENT_TYPES.contains(contentType);
    }

    @Override
    public boolean isValidFileSize(MultipartFile file) {
        return file.getSize() <= maxFileSize;
    }

    private void createUploadDirectoryIfNotExists() {
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }
}
