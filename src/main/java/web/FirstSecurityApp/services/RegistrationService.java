package web.FirstSecurityApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.FirstSecurityApp.models.User;
import web.FirstSecurityApp.repositories.UserRepository;

@Service
public class RegistrationService {
    private final UserRepository userRepository;

    @Autowired
    public RegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void registerUser(User user) {
        user.setRole("ROLE_USER");
        userRepository.save(user);
    }
}
