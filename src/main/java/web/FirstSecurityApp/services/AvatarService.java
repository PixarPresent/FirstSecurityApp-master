package web.FirstSecurityApp.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import web.FirstSecurityApp.repositories.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class AvatarService {
    private final UserRepository userRepository;

    public AvatarService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void storeAvatar(MultipartFile file, String username) {
        validateImage(file);

        String ext = Objects.requireNonNull(file.getOriginalFilename())
                .substring(file.getOriginalFilename().lastIndexOf(".") + 1);

        String filename = UUID.randomUUID() + "." + ext;
        Path uploadDir = Paths.get("user-avatars");
        try {
            Files.createDirectories(uploadDir);
            file.transferTo(uploadDir.resolve(filename));
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении файла", e);
        }

        userRepository.updateAvatarPath(username, filename);
    }

    public Path getAvatar(String username) {
        return userRepository.findByUsername(username)
                .map(u -> Paths.get("user-avatars", u.getAvatarPath()))
                .orElse(null);
    }

    private void validateImage(MultipartFile file) {
        if (file.isEmpty()) throw new IllegalArgumentException("Файл пустой");
        if (file.getSize() > 2 * 1024 * 1024) throw new IllegalArgumentException("Файл слишком большой");
        if (!List.of("image/jpeg", "image/png", "image/webp").contains(file.getContentType()))
            throw new IllegalArgumentException("Неподдерживаемый формат");
    }
}
