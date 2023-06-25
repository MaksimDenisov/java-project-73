package hexlet.code.app.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.app.config.SpringConfigForIT;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.utils.TestUtils;
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
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static hexlet.code.app.config.SpringConfigForIT.TEST_PROFILE;
import static hexlet.code.app.controller.TaskStatusController.TASK_STATUS_CONTROLLER_PATH;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfigForIT.class)
public class TaskStatusControllerTest {

    @Autowired
    TestUtils utils;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskStatusRepository repository;

    @BeforeEach
    public void setUp() throws IOException {
        utils.saveTaskStatuses();
    }

    @AfterEach
    public void tearDown() {
        utils.tearDown();
    }


    @Test
    @DisplayName("Get all status.")
    public void shouldGetAll() throws Exception {
        final var response = mockMvc.perform(get(TASK_STATUS_CONTROLLER_PATH))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final List<TaskStatus> statuses = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertThat(statuses).hasSize(2);
    }

    @Test
    @DisplayName("Get status by id.")
    public void shouldGetById() throws Exception {
        final TaskStatus expectedTaskStatus = repository.findAll().get(0);
        final var response = mockMvc.perform(get(TASK_STATUS_CONTROLLER_PATH + ID, expectedTaskStatus.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final TaskStatus actualTaskStatus = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertEquals(expectedTaskStatus.getId(), actualTaskStatus.getId());
        assertEquals(expectedTaskStatus.getName(), actualTaskStatus.getName());
        assertEquals(expectedTaskStatus.getCreatedAt(), actualTaskStatus.getCreatedAt());
    }

    @Test
    @DisplayName("Get 204 (No Content) when status by id not found.")
    public void shouldReturnNoContentWhenStatusWithIDNotFound() throws Exception {
        mockMvc.perform(get(TASK_STATUS_CONTROLLER_PATH + ID, -1))
                .andExpect(status().isNoContent())
                .andReturn()
                .getResponse();
    }

    @Test
    @DisplayName("Create status.")
    public void create() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("name", "Новый");
        assertEquals(2, repository.count());
        final var response = mockMvc.perform(post(TASK_STATUS_CONTROLLER_PATH)
                        .content(asJson(map))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();
        assertEquals(3, repository.count());
        final TaskStatus actualTaskStatus = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertEquals("Новый", actualTaskStatus.getName());
    }

    @Test
    @DisplayName("Get all status.")
    public void shouldDeleteStatus() throws Exception {
        assertEquals(2, repository.count());
        final TaskStatus expectedStatus = repository.findAll().get(0);
        mockMvc.perform(delete(TASK_STATUS_CONTROLLER_PATH + ID, expectedStatus.getId()))
                .andExpect(status().isNoContent())
                .andReturn()
                .getResponse();
        assertEquals(1, repository.count());
    }
}
