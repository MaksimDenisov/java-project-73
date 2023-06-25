package hexlet.code.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Label {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name; // обязательное. Минимум 1 символ. Названия меток могу быть любыми
    private LocalDateTime createdAt; // дата создания метки
}
