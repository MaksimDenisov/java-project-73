package hexlet.code.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.TemporalType.TIMESTAMP;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id; // уникальный идентификатор пользователя, генерируется автоматически

    @NotBlank
    private String firstName; // имя пользователя

    @NotBlank
    private String lastName; // фамилия пользователя

    @Email
    @Column(unique = true)
    private String email; //  - адрес электронной почты

    @NotBlank
    @JsonIgnore
    private String password; //  - пароль

    @CreationTimestamp
    @Temporal(TIMESTAMP)
    private Instant createdAt; //  заполняется автоматически. Дата создания задачи
}
