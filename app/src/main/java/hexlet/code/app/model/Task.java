package hexlet.code.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.TemporalType.TIMESTAMP;

@Entity
@Table(name = "tasks")
@Getter
@Setter
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
    private Date createdAt; //  заполняется автоматически. Дата создания задачи
}
