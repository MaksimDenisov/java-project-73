package hexlet.code.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static hexlet.code.utils.TestUtils.fromJson;

@Component
public class TestData {

    private static final String RESOURCES = "src/test/resources/";

    public static final String FIRST_USER_MAIL = "ivan@google.com";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private LabelRepository labelRepository;


    public void clearAll() {
        taskRepository.deleteAll();
        taskStatusRepository.deleteAll();
        labelRepository.deleteAll();
        userRepository.deleteAll();
    }

    public List<User> getUsers() throws IOException {
        String usersJson = Files.readString(getExpectedPath("users.json"));
        return fromJson(usersJson, new TypeReference<>() {
        });
    }

    public void registerUsers() throws IOException {
        getUsers().stream()
                .peek(user -> user.setPassword("password"))
                .forEach(user -> userRepository.save(user));
    }

    // Populate task statuses
    public void saveTaskStatuses() throws IOException {
        taskStatusRepository.saveAll(getTaskStatuses());
    }

    public List<TaskStatus> getTaskStatuses() throws IOException {
        return fromJson(Files.readString(getExpectedPath("taskStatuses.json")), new TypeReference<>() {
        });
    }

    // Populate tasks
    public void saveTasks() throws IOException {
        taskRepository.saveAll(getTasks());
    }

    public List<Task> getTasks() throws IOException {
        return fromJson(Files.readString(getExpectedPath("tasks.json")), new TypeReference<>() {
        });
    }

    public void saveLabels() throws IOException {
        labelRepository.saveAll(getLabels());
    }

    public List<Label> getLabels() throws IOException {
        return fromJson(Files.readString(getExpectedPath("labels.json")), new TypeReference<>() {
        });
    }

    public Task getNewTask() throws IOException {
        return fromJson(Files.readString(getExpectedPath("createdTask.json")), new TypeReference<>() {
        });
    }

    private static Path getExpectedPath(String filename) {
        return Paths.get(RESOURCES + "fixtures/" + filename)
                .toAbsolutePath()
                .normalize();
    }

}
