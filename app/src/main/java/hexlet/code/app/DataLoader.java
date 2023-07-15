package hexlet.code.app;

import hexlet.code.app.dto.UserTO;
import hexlet.code.app.model.Label;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

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
        UserTO user1 = new UserTO("ivanov@mail.com", "Ivan", "Ivanov", "pass");
        UserTO user2 = new UserTO("petrov@mail.com", "Petr", "Petrov", "pass");
        User ivanov = userService.create(user1);
        User petrov = userService.create(user2);

        TaskStatus taskStatusNew = new TaskStatus("Новый");
        TaskStatus taskStatusWork = new TaskStatus("В работе");
        taskStatusRepository.save(taskStatusNew);
        taskStatusRepository.save(taskStatusWork);

        Label labelBug = new Label("Баг");
        Label labelFeature = new Label("Фича");
        labelRepository.save(labelBug);
        labelRepository.save(labelFeature);

        Task task = Task.builder()
                .name("Первая задача")
                .description("Сделать что-то")
                .author(ivanov)
                .executor(petrov)
                .taskStatus(taskStatusNew)
                .label(List.of(labelFeature))
                .build();
        taskRepository.save(task);
    }
}
