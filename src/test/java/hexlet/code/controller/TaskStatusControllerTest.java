package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.config.SpringConfigForIT;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static hexlet.code.config.SpringConfigForIT.TEST_PROFILE;
import static hexlet.code.controller.TaskStatusController.ID;
import static hexlet.code.controller.TaskStatusController.TASK_STATUS_CONTROLLER_PATH;
import static hexlet.code.utils.TestData.FIRST_USER_MAIL;
import static hexlet.code.utils.TestUtils.asJson;
import static hexlet.code.utils.TestUtils.fromJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfigForIT.class)
public class TaskStatusControllerTest {

    @Autowired
    private TestUtils utils;

    @Autowired
    private TestData data;

    @Autowired
    private TaskStatusRepository repository;

    @BeforeEach
    public void setUp() throws IOException {
        data.saveTaskStatuses();
    }

    @AfterEach
    public void tearDown() {
        data.clearAll();
    }

    @Test
    @DisplayName("Get all status. Available for all users.")
    public void testGetAll() throws Exception {
        final var response = utils.perform(get(TASK_STATUS_CONTROLLER_PATH))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final List<TaskStatus> statuses = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertThat(statuses).hasSize(2);
    }

    @Test
    @DisplayName("Get status by id. Available for all users.  If status not found get 204 (No Content)")
    public void testGetOne() throws Exception {
        final TaskStatus expectedTaskStatus = repository.findAll().get(0);
        final var response = utils.perform(
                get(TASK_STATUS_CONTROLLER_PATH + ID, expectedTaskStatus.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final TaskStatus actualTaskStatus = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertEquals(expectedTaskStatus.getId(), actualTaskStatus.getId());
        assertEquals(expectedTaskStatus.getName(), actualTaskStatus.getName());
        assertEquals(0, expectedTaskStatus.getCreatedAt().compareTo(actualTaskStatus.getCreatedAt()));

        utils.perform(get(TASK_STATUS_CONTROLLER_PATH + ID, -1))
                .andExpect(status().isNoContent())
                .andReturn()
                .getResponse();
    }

    @Test
    @DisplayName("Create status. Available for authorized users.")
    public void testCreate() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("name", "Новый");

        utils.checkNotAuthorizedRequestIsForbidden(post(TASK_STATUS_CONTROLLER_PATH)
                .content(asJson(map))
                .contentType(APPLICATION_JSON));

        assertEquals(2, repository.count());
        final var response = utils.performByUser(post(TASK_STATUS_CONTROLLER_PATH)
                        .content(asJson(map))
                        .contentType(APPLICATION_JSON), FIRST_USER_MAIL)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();
        assertEquals(3, repository.count());
        final TaskStatus actualTaskStatus = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertEquals("Новый", actualTaskStatus.getName());
    }

    @Test
    @DisplayName("Update task status. Available for authorized users.")
    public void testUpdate() throws Exception {
        TaskStatus status = repository.findAll().get(0);
        Map<String, String> map = new HashMap<>();
        map.put("name", "Updated");
        utils.checkNotAuthorizedRequestIsForbidden(
                put(TASK_STATUS_CONTROLLER_PATH + ID, status.getId())
                        .content(TestUtils.asJson(map))
                        .contentType(APPLICATION_JSON));

        utils.performByUser(
                        put(TASK_STATUS_CONTROLLER_PATH + ID, status.getId())
                                .content(TestUtils.asJson(map))
                                .contentType(APPLICATION_JSON), FIRST_USER_MAIL)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final TaskStatus actualStatus = repository.findById(status.getId()).orElse(null);
        assertNotNull(actualStatus);
        assertEquals("Updated", actualStatus.getName());
    }

    @Test
    @DisplayName("Delete status. Available for authorized users.")
    public void testDelete() throws Exception {
        final TaskStatus expectedStatus = repository.findAll().get(0);
        assertEquals(2, repository.count());

        utils.checkNotAuthorizedRequestIsForbidden(
                delete(TASK_STATUS_CONTROLLER_PATH + ID, expectedStatus.getId()));

        utils.performByUser(
                delete(TASK_STATUS_CONTROLLER_PATH + ID, expectedStatus.getId()), FIRST_USER_MAIL)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        assertEquals(1, repository.count());
    }
}
