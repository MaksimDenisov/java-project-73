package hexlet.code.app;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Setter
@Getter
public class User {
    @Id
    private long id; // уникальный идентификатор пользователя, генерируется автоматически
    private String firstName; // имя пользователя
    private String lastName; // фамилия пользователя
    private String email; //  - адрес электронной почты
    private String password; //  - пароль
    private LocalDateTime createdAt; // дата создания (регистрации) пользователя
}
