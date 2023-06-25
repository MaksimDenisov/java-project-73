package hexlet.code.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "task_statuses")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TaskStatus {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private LocalDateTime createdAt;

    public TaskStatus(String name) {
        this.id = null;
        this.name = name;
        this.createdAt = LocalDateTime.now();
    }
}
