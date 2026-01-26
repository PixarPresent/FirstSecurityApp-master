package web.FirstSecurityApp.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.FirstSecurityApp.models.Media;
import web.FirstSecurityApp.services.MediaService;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/media")
@CrossOrigin(origins = "*")
public class MediaController {

    private final MediaService mediaService;

    @Value("${app.media.upload-dir:uploads/media}")
    private String uploadDir;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> getMediaFile(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get(uploadDir, fileName);
            Resource resource = new FileSystemResource(filePath);

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            Media media = mediaService.getMediaByFileName(fileName);
            if (media == null) {
                return ResponseEntity.notFound().build();
            }

            String contentType = media.getContentType();
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + media.getOriginalFileName() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/info/{mediaId}")
    public ResponseEntity<Media> getMediaInfo(@PathVariable Long mediaId) {
        Media media = mediaService.getMediaById(mediaId);
        return ResponseEntity.ok(media);
    }

    @DeleteMapping("/{mediaId}")
    public ResponseEntity<Void> deleteMedia(@PathVariable Long mediaId) {
        mediaService.deleteMedia(mediaId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<java.util.List<Media>> getMediaByPostId(@PathVariable Long postId) {
        java.util.List<Media> mediaFiles = mediaService.getMediaByPostId(postId);
        return ResponseEntity.ok(mediaFiles);
    }
}
