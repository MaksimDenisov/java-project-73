package hexlet.code.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TaskTO {
    private String name;
    private String description;
    private long executorId;
    private long taskStatusId;
}
