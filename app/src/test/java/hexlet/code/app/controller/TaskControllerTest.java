package hexlet.code.app.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.app.config.SpringConfigForIT;
import hexlet.code.app.dto.TaskTO;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static hexlet.code.app.config.SpringConfigForIT.TEST_PROFILE;
import static hexlet.code.app.controller.TaskController.TASKS_CONTROLLER_PATH;
import static hexlet.code.app.controller.UserController.ID;
import static hexlet.code.app.utils.TestUtils.asJson;
import static hexlet.code.app.utils.TestUtils.fromJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfigForIT.class)
public class TaskControllerTest {
    @Autowired
    TestUtils utils;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() throws Exception {
        utils.registerUsers();
        List<User> users = userRepository.findAll();
        users.forEach(System.out::println);
        utils.saveTaskStatuses();
        utils.saveTasks();
    }

    @AfterEach
    public void tearDown() {
        utils.tearDown();
    }

    @Test
    @DisplayName("Getting a list of tasks")
    public void shouldGetTasks() throws Exception {
        final var response = mockMvc.perform(get(TASKS_CONTROLLER_PATH))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final List<Task> tasks = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertThat(tasks).hasSize(2);
    }

    @Test
    @DisplayName("Getting a task by id")
    public void shouldGetTaskById() throws Exception {
        final Task expectedTask = taskRepository.findAll().get(0);
        final var response = mockMvc.perform(get(TASKS_CONTROLLER_PATH + ID, expectedTask.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final Task actualTask = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertEquals(expectedTask.getId(), actualTask.getId());
        assertEquals(expectedTask.getName(), actualTask.getName());
        assertEquals(expectedTask.getCreatedAt(), actualTask.getCreatedAt());
    }

    @Test
    @DisplayName("Creating a new task")
    public void shouldCreateNewTask() throws Exception {
        TaskTO expectedTO = new TaskTO("Новое имя", "Новое описание", 2, 2);
        final var response = mockMvc.perform(post(TASKS_CONTROLLER_PATH)
                        .content(asJson(expectedTO))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();
        assertEquals(3, taskRepository.count());

        final Task actualTask = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertEquals(expectedTO.getName(), actualTask.getName());
        assertEquals(expectedTO.getDescription(), actualTask.getDescription());
        assertEquals(expectedTO.getTaskStatusId(), actualTask.getTaskStatus().getId());
        assertEquals(expectedTO.getExecutorId(), actualTask.getAuthor().getId());
    }

    @Test
    @DisplayName("Updating a task")
    public void shouldUpdateTask() throws Exception {
        long expectedId = taskRepository.findAll().get(0).getId();
        TaskTO expectedTO = new TaskTO("Новое имя", "Новое описание", 2, 2);
        mockMvc.perform(put(TASKS_CONTROLLER_PATH + ID, expectedId)
                        .content(asJson(expectedTO))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals(2, taskRepository.count());

        final var response = mockMvc.perform(get(TASKS_CONTROLLER_PATH + ID, expectedId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final Task actualTask = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertEquals(expectedTO.getName(), actualTask.getName());
        assertEquals(expectedTO.getDescription(), actualTask.getDescription());
        assertEquals(expectedTO.getTaskStatusId(), actualTask.getTaskStatus().getId());
        assertEquals(expectedTO.getExecutorId(), actualTask.getExecutor().getId());
    }

    @Test
    @DisplayName("Deleting a task")
    public void shouldDeleteTask() throws Exception {
        assertThat(taskRepository.findAll()).hasSize(2);
        mockMvc.perform(delete(TASKS_CONTROLLER_PATH + ID, 1))
                .andExpect(status().isNoContent())
                .andReturn()
                .getResponse();
        assertThat(taskRepository.findAll()).hasSize(1);
    }

}