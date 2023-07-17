package hexlet.code.app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class TaskTO {
    @NotBlank
    private String name;
    private String description;
    private long executorId;
    private long taskStatusId;
    private List<Long> labelIds;
}
