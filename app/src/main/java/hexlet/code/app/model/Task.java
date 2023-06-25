package hexlet.code.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;


@Entity
@Getter
@Setter
@Table(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotEmpty
    private String name; // обязательное. Минимум 1 символ. Названия задач могут быть любыми

    private String description; // необязательное. Описание задачи, может быть любым

    @NotNull
    @OneToOne
    @JoinColumn(name = "task_status_id")
    private TaskStatus taskStatus; //  обязательное. Связано с сущностью статуса

    @NotNull
    @OneToOne
    @JoinColumn(name = "author_id")
    private User author; //  обязательное. Создатель задачи, связан с сущностью пользователя

    @OneToOne
    @JoinColumn(name = "executor_id")
    private User executor; //  необязательное. Исполнитель задачи, связан с сущностью пользователя

    private LocalDateTime createdAt; //  заполняется автоматически. Дата создания задачи
}
