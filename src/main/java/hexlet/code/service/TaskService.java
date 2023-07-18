package hexlet.code.service;


import com.querydsl.core.types.Predicate;
import hexlet.code.dto.TaskTO;
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

    public Task create(TaskTO taskTO) {
        Set<Label> labels = labelService.getByIds(taskTO.getLabelIds());
        Task task = Task.builder()
                .name(taskTO.getName())
                .description(taskTO.getDescription())
                .author(userService.getCurrentUser())
                .executor(userService.getById(taskTO.getExecutorId()))
                .taskStatus(taskStatusService.getById(taskTO.getTaskStatusId()))
                .labels(labels)
                .build();
        return taskRepository.save(task);
    }

    public void delete(Long id) {
        taskRepository.findById(id).ifPresent(taskRepository::delete);
    }

    public Task update(long id, TaskTO taskTO) {
        Task task = getById(id);
        task.setName(taskTO.getName());
        task.setDescription(taskTO.getDescription());
        task.setExecutor(userService.getById(taskTO.getExecutorId()));
        task.setLabels(labelService.getByIds(taskTO.getLabelIds()));
        task.setTaskStatus(taskStatusService.getById(taskTO.getTaskStatusId()));
        return taskRepository.save(task);
    }
}
