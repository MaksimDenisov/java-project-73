package hexlet.code.service;

import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TaskStatusService {
    private final TaskStatusRepository taskStatusRepository;

    public TaskStatus getById(Long id) {
        return taskStatusRepository.findById(id).orElseThrow();
    }

    public List<TaskStatus> getAll() {
        return taskStatusRepository.findAll();
    }

    public TaskStatus create(String name) {
        return taskStatusRepository.save(new TaskStatus(name));
    }

    public void delete(Long id) {
        taskStatusRepository.deleteById(id);
    }

    public TaskStatus update(long id, String name) {
        TaskStatus status = getById(id);
        status.setName(name);
        return taskStatusRepository.save(status);
    }
}
