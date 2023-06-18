package hexlet.code.app.service;

import hexlet.code.app.dto.UserTO;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserTO getById(Long id) {
        return userRepository.findById(id)
                .map(this::toDto)
                .orElse(null);
    }

    public List<UserTO> getAll() {
        return userRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public UserTO create(String firstName, String lastName, String email, String password) {
        return toDto(userRepository.save(new User(null, firstName, lastName,
                email, passwordEncoder.encode(password),
                LocalDateTime.now())));
    }

    public UserTO update(Long id, String firstName, String lastName, String email, String password) {
        return toDto(userRepository.save(new User(id, firstName, lastName,
                email, passwordEncoder.encode(password),
                LocalDateTime.now())));
    }

    public void delete(Long id) {
        userRepository.findById(id).ifPresent(userRepository::delete);
    }

    private UserTO toDto(User user) {
        return new UserTO(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getCreatedAt());
    }


}
