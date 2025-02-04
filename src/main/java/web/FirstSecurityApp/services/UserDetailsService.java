package web.FirstSecurityApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import web.FirstSecurityApp.models.User;
import web.FirstSecurityApp.repositories.UserRepository;
import web.FirstSecurityApp.security.UserDetails;

import java.util.Optional;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {


    private final UserRepository userRepository;

    @Autowired
    public UserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> person = userRepository.findByUsername(username);

        if (person.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        return new UserDetails(person.get());
    }
}
