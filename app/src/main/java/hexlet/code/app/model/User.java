package hexlet.code.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id; // уникальный идентификатор пользователя, генерируется автоматически
    @NotBlank
    private String firstName; // имя пользователя
    @NotBlank
    private String lastName; // фамилия пользователя
    @Email
    private String email; //  - адрес электронной почты
    @NotBlank
    @JsonIgnore
    private String password; //  - пароль

    private LocalDateTime createdAt; // дата создания (регистрации) пользователя
}
