package hexlet.code.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.TemporalType.TIMESTAMP;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Label {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotEmpty
    private String name; // обязательное. Минимум 1 символ. Названия меток могу быть любыми

    @CreationTimestamp
    @Temporal(TIMESTAMP)
    private Date createdAt;

    public Label(String name) {
        this.name = name;
    }
}
