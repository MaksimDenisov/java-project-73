package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TaskDTO {
    @NotBlank
    private String name;
    private String description;
    private long executorId;
    @NotNull
    private long taskStatusId;
    private List<Long> labelIds;
}
