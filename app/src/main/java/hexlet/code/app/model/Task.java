package hexlet.code.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.TemporalType.TIMESTAMP;


@Entity
@Getter
@Setter
@Table(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @ManyToMany
    private List<Label> label;

    @CreationTimestamp
    @Temporal(TIMESTAMP)
    private Date createdAt;//  заполняется автоматически. Дата создания задачи
}
