package hexlet.code.app.service;

import hexlet.code.app.dto.UserTO;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElse(null);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User create(UserTO user) {
        return userRepository.save(new User(null, user.getFirstName(), user.getLastName(),
                user.getEmail(), passwordEncoder.encode(user.getPassword()),
                LocalDateTime.now()));
    }

    public User update(Long id, UserTO user) {
        return userRepository.save(new User(id, user.getFirstName(), user.getLastName(),
                user.getEmail(), passwordEncoder.encode(user.getPassword()),
                LocalDateTime.now()));
    }

    public void delete(Long id) {
        userRepository.findById(id).ifPresent(userRepository::delete);
    }

    public String getCurrentUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public User getCurrentUser() {
        String currentUser  = getCurrentUserName();
        return userRepository.findByEmail(currentUser).get();
    }
}
