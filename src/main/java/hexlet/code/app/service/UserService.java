package hexlet.code.app.service;

import hexlet.code.app.dto.UserTO;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public User create(UserTO userTO) {
        User user = User.builder()
                .firstName(userTO.getFirstName())
                .lastName(userTO.getLastName())
                .email(userTO.getEmail())
                .password(passwordEncoder.encode(userTO.getPassword()))
                .build();
        return userRepository.save(user);
    }

    public User update(Long id, UserTO userTO) {
        User user = User.builder()
                .id(id)
                .firstName(userTO.getFirstName())
                .lastName(userTO.getLastName())
                .email(userTO.getEmail())
                .password(userTO.getPassword())
                .build();
        return userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.findById(id).ifPresent(userRepository::delete);
    }

    public String getCurrentUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public User getCurrentUser() {
        String currentUser = getCurrentUserName();
        return userRepository.findByEmail(currentUser).get();
    }
}
