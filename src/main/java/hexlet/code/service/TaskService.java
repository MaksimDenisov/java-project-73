package hexlet.code.service;


import com.querydsl.core.types.Predicate;
import hexlet.code.dto.TaskDTO;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final TaskStatusService taskStatusService;
    private final LabelService labelService;

    public Task getById(Long id) {
        return taskRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Not found status with id %d", id)));
    }

    public List<Task> getAll(Predicate predicate) {
        List<Task> tasks = new ArrayList<>();
        taskRepository.findAll(predicate).forEach(tasks::add);
        return tasks;
    }

    public Task create(TaskDTO taskDTO) {
        Set<Label> labels = labelService.getByIds(taskDTO.getLabelIds());
        Task task = Task.builder()
                .name(taskDTO.getName())
                .description(taskDTO.getDescription())
                .author(userService.getCurrentUser())
                .executor(userService.getById(taskDTO.getExecutorId()))
                .taskStatus(taskStatusService.getById(taskDTO.getTaskStatusId()))
                .labels(labels)
                .build();
        return taskRepository.save(task);
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    public Task update(long id, TaskDTO taskDTO) {
        Task task = getById(id);
        task.setName(taskDTO.getName());
        task.setDescription(taskDTO.getDescription());
        task.setExecutor(userService.getById(taskDTO.getExecutorId()));
        task.setLabels(labelService.getByIds(taskDTO.getLabelIds()));
        task.setTaskStatus(taskStatusService.getById(taskDTO.getTaskStatusId()));
        return taskRepository.save(task);
    }
}
