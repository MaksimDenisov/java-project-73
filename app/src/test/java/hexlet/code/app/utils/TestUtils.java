package hexlet.code.app.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.model.Label;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
public class TestUtils {
    private static final String RESOURCES = "src/test/resources/";
    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private LabelRepository labelRepository;


    public void tearDown() {
        labelRepository.deleteAll();
        taskRepository.deleteAll();
        taskStatusRepository.deleteAll();
        userRepository.deleteAll();
    }

    // Populate users
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

    // Utils for working with files.

    private static Path getExpectedPath(String filename) {
        return Paths.get(RESOURCES + "fixtures/" + filename)
                .toAbsolutePath()
                .normalize();
    }

    @Deprecated
    private String getResourcePath(String fixtureName) {
        return RESOURCES + fixtureName;
    }

    public static String asJson(final Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);
    }

    public static <T> T fromJson(final String json, final TypeReference<T> to) throws JsonProcessingException {
        return MAPPER.readValue(json, to);
    }


}
