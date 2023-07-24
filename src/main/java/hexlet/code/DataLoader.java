package hexlet.code;

import hexlet.code.dto.UserDTO;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Set;

@Profile("dev")
@Component
@AllArgsConstructor
public class DataLoader implements ApplicationRunner {

    private UserService userService;
    private TaskStatusRepository taskStatusRepository;
    private LabelRepository labelRepository;
    private TaskRepository taskRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        UserDTO user1 = new UserDTO("ivanov@mail.com", "Ivan", "Ivanov", "pass");
        UserDTO user2 = new UserDTO("petrov@mail.com", "Petr", "Petrov", "pass");
        User ivanov = userService.create(user1);
        User petrov = userService.create(user2);

        TaskStatus taskStatusNew = new TaskStatus("Новый");
        TaskStatus taskStatusWork = new TaskStatus("В работе");
        taskStatusRepository.save(taskStatusNew);
        taskStatusRepository.save(taskStatusWork);

        Label labelBug = labelRepository.save(new Label("Баг"));
        Label labelFeature = labelRepository.save(new Label("Фича"));

        Task task = Task.builder()
                .name("Первая задача")
                .description("Сделать что-то")
                .author(ivanov)
                .executor(petrov)
                .taskStatus(taskStatusNew)
                .labels(Set.of(labelBug, labelFeature))
                .build();
        taskRepository.save(task);
    }
}
