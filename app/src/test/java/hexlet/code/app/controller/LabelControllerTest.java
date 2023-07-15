package hexlet.code.app.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.app.config.SpringConfigForIT;
import hexlet.code.app.model.Label;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.LabelRepository;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static hexlet.code.app.config.SpringConfigForIT.TEST_PROFILE;
import static hexlet.code.app.controller.LabelController.LABEL_CONTROLLER_PATH;
import static hexlet.code.app.controller.UserController.ID;
import static hexlet.code.app.utils.TestUtils.USER_MAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfigForIT.class)
public class LabelControllerTest {
    @Autowired
    private TestUtils utils;

    @Autowired
    private LabelRepository repository;

    @BeforeEach
    public void setUp() throws IOException {
        utils.saveLabels();
    }

    @AfterEach
    public void tearDown() {
        utils.tearDown();
    }

    @Test
    @DisplayName("Get all labels.")
    public void shouldGetAll() throws Exception {
        final var response = utils.perform(get(LABEL_CONTROLLER_PATH), USER_MAIL)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final List<Label> statuses = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertThat(statuses).hasSize(2);
    }

    @Test
    @DisplayName("Unauthorized get all labels.")
    public void shouldNotGetAll() throws Exception {
        final var response = utils.perform(get(LABEL_CONTROLLER_PATH))
                .andExpect(status().isForbidden())
                .andReturn()
                .getResponse();
    }

    @Test
    @DisplayName("Get label by id.")
    public void shouldGetById() throws Exception {
        final Label expectedLabel = repository.findAll().get(0);
        final var response = utils.perform(get(LABEL_CONTROLLER_PATH + ID, expectedLabel.getId()), USER_MAIL)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final Label actualLabel = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertEquals(expectedLabel.getId(), actualLabel.getId());
        assertEquals(expectedLabel.getName(), actualLabel.getName());
        assertEquals(0, expectedLabel.getCreatedAt().compareTo(actualLabel.getCreatedAt()));
    }

    @Test
    @DisplayName("Unauthorized get label by id.")
    public void shouldNotGetById() throws Exception {
        final Label expectedLabel = repository.findAll().get(0);
        utils.perform(get(LABEL_CONTROLLER_PATH + ID, expectedLabel.getId()))
                .andExpect(status().isForbidden())
                .andReturn()
                .getResponse();
    }

    @Test
    @DisplayName("Get 204 (No Content) when label by id not found.")
    public void shouldReturnNoContentWhenStatusWithIDNotFound() throws Exception {
        utils.perform(get(LABEL_CONTROLLER_PATH + ID, -1), USER_MAIL)
                .andExpect(status().isNoContent())
                .andReturn()
                .getResponse();
    }

    @Test
    @DisplayName("Create label.")
    public void shouldCreate() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("name", "Новая метка");
        assertEquals(2, repository.count());
        final var response = utils.perform(post(LABEL_CONTROLLER_PATH)
                        .content(TestUtils.asJson(map))
                        .contentType(APPLICATION_JSON), USER_MAIL)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();
        assertEquals(3, repository.count());
        final TaskStatus actualLabel = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertEquals("Новая метка", actualLabel.getName());
    }

    @Test
    @DisplayName("Unauthorized create label.")
    public void shouldNotCreate() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("name", "Новая метка");
        assertEquals(2, repository.count());
        final var response = utils.perform(post(LABEL_CONTROLLER_PATH)
                        .content(TestUtils.asJson(map))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn()
                .getResponse();
    }

    @Test
    @DisplayName("Delete label.")
    public void shouldDelete() throws Exception {
        assertEquals(2, repository.count());
        final Label expectedLabel = repository.findAll().get(0);
        utils.perform(delete(LABEL_CONTROLLER_PATH + ID, expectedLabel.getId()), USER_MAIL)
                .andExpect(status().isNoContent())
                .andReturn()
                .getResponse();
        assertEquals(1, repository.count());
    }

    @Test
    @DisplayName("Unauthorized delete label.")
    public void shouldNotDelete() throws Exception {
        final Label expectedLabel = repository.findAll().get(0);
        utils.perform(delete(LABEL_CONTROLLER_PATH + ID, expectedLabel.getId()))
                .andExpect(status().isForbidden())
                .andReturn()
                .getResponse();
    }
}
