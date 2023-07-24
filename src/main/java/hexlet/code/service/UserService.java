package hexlet.code.service;

import hexlet.code.dto.UserDTO;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
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

    public User create(UserDTO userDTO) {
        User user = User.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .build();
        return userRepository.save(user);
    }

    public User update(Long id, UserDTO userDTO) {
        User user = User.builder()
                .id(id)
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
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
