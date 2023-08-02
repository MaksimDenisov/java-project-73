package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.config.SpringConfigForIT;
import hexlet.code.dto.TaskDTO;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.utils.TestData;
import hexlet.code.utils.TestUtils;
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

import java.util.List;
import java.util.stream.Collectors;

import static hexlet.code.config.SpringConfigForIT.TEST_PROFILE;
import static hexlet.code.controller.TaskController.ID;
import static hexlet.code.controller.TaskController.TASKS_CONTROLLER_PATH;
import static hexlet.code.utils.TestData.FIRST_USER_MAIL;
import static hexlet.code.utils.TestUtils.asJson;
import static hexlet.code.utils.TestUtils.fromJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    private TestUtils utils;

    @Autowired
    private TestData data;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private LabelRepository labelRepository;

    @BeforeEach
    public void setUp() throws Exception {
        data.registerUsers();
        data.saveTaskStatuses();
        data.saveTasks();
        data.saveLabels();
    }

    @AfterEach
    public void tearDown() {
        data.clearAll();
    }

    @Test
    @DisplayName("Getting a list of tasks")
    public void testGetAll() throws Exception {
        final var response = utils.performByUser(get(TASKS_CONTROLLER_PATH), FIRST_USER_MAIL)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final List<Task> tasks = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        final List<Task> expected = taskRepository.findAll();
        assertThat(tasks)
                .usingRecursiveFieldByFieldElementComparatorOnFields("id", "name", "description")
                .containsAll(expected);
    }

    @Test
    @DisplayName("Getting a task by id")
    public void testGetOne() throws Exception {
        final Task expectedTask = taskRepository.findAll().get(0);
        final var response = utils.performByUser(
                        get(TASKS_CONTROLLER_PATH + ID, expectedTask.getId()), FIRST_USER_MAIL)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final Task actualTask = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertEquals(expectedTask.getId(), actualTask.getId());
        assertEquals(expectedTask.getName(), actualTask.getName());
        assertEquals(0, expectedTask.getCreatedAt().compareTo(actualTask.getCreatedAt()));
    }

    @Test
    @DisplayName("Creating a new task")
    public void testCreate() throws Exception {
        List<Label> expectedLabels = labelRepository.findAll();
        TaskDTO expectedTO = new TaskDTO("Новое имя", "Новое описание", 2, 2,
                expectedLabels.stream()
                        .map(Label::getId)
                        .collect(Collectors.toList()));
        final var response = utils.performByUser(post(TASKS_CONTROLLER_PATH)
                        .content(asJson(expectedTO))
                        .contentType(APPLICATION_JSON), FIRST_USER_MAIL)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        final Task resultTask = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        final Task actualTask = taskRepository.findById(resultTask.getId()).orElse(null);

        assertNotNull(actualTask);
        assertThat(actualTask.getLabels())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("tasks")
                .containsAll(expectedLabels);
        assertEquals(expectedTO.getName(), actualTask.getName());
        assertEquals(expectedTO.getDescription(), actualTask.getDescription());
        assertEquals(expectedTO.getTaskStatusId(), actualTask.getTaskStatus().getId());
        assertEquals(expectedTO.getExecutorId(), actualTask.getExecutor().getId());
    }

    @Test
    @DisplayName("Updating a task")
    public void testUpdate() throws Exception {
        long expectedId = taskRepository.findAll().get(0).getId();
        List<Label> expectedLabels = labelRepository.findAll();
        TaskDTO expectedTO = new TaskDTO("Новое имя", "Новое описание", 2, 2,
                expectedLabels.stream().map(Label::getId).collect(Collectors.toList()));
        utils.performByUser(put(TASKS_CONTROLLER_PATH + ID, expectedId)
                        .content(asJson(expectedTO))
                        .contentType(APPLICATION_JSON), FIRST_USER_MAIL)
                .andExpect(status().isOk());

        final var response = utils.performByUser(
                        get(TASKS_CONTROLLER_PATH + ID, expectedId), FIRST_USER_MAIL)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final Task resultTask = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        final Task actualTask = taskRepository.findById(resultTask.getId()).orElse(null);

        assertNotNull(actualTask);
        assertThat(actualTask.getLabels())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("tasks")
                .containsAll(expectedLabels);
        assertEquals(expectedTO.getName(), actualTask.getName());
        assertEquals(expectedTO.getDescription(), actualTask.getDescription());
        assertEquals(expectedTO.getTaskStatusId(), actualTask.getTaskStatus().getId());
        assertEquals(expectedTO.getExecutorId(), actualTask.getExecutor().getId());
    }

    @Test
    @DisplayName("Deleting a task")
    public void testDelete() throws Exception {
        Long id = taskRepository.findAll().get(0).getId();
        utils.performByUser(delete(TASKS_CONTROLLER_PATH + ID, -1), FIRST_USER_MAIL)
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();

        utils.performByUser(delete(TASKS_CONTROLLER_PATH + ID, id), FIRST_USER_MAIL)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        assertFalse(taskRepository.existsById(id));
    }
}
