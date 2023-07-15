package hexlet.code.app.service;

import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.service.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class TaskStatusService {
    private final TaskStatusRepository taskStatusRepository;

    public TaskStatus getById(Long id) {
        return taskStatusRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Not found status with id %d", id)));
    }

    public List<TaskStatus> getAll() {
        return taskStatusRepository.findAll();
    }

    public TaskStatus create(String name) {
        return taskStatusRepository.save(new TaskStatus(name));
    }

    public void delete(Long id) {
        taskStatusRepository.findById(id).ifPresent(taskStatusRepository::delete);
    }

    public TaskStatus update(long id, String name) {
        TaskStatus status = getById(id);
        status.setName(name);
        return taskStatusRepository.save(status);
    }
}
