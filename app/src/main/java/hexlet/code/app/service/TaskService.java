package hexlet.code.app.service;


import hexlet.code.app.dto.TaskTO;
import hexlet.code.app.model.Task;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.service.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final TaskStatusService taskStatusService;

    public Task getById(Long id) {
        return taskRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Not found status with id %d", id)));
    }

    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    public Task create(TaskTO taskTO) {
        Task task = new Task(null, taskTO.getName(),
                taskTO.getDescription(),
                taskStatusService.getById(taskTO.getTaskStatusId()),
                userService.getById(taskTO.getExecutorId()), // TODO Get me
                userService.getById(taskTO.getExecutorId()),
                LocalDateTime.now());
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
        task.setTaskStatus(taskStatusService.getById(taskTO.getTaskStatusId()));
        return taskRepository.save(task);
    }
}
