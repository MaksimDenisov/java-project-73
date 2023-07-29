package hexlet.code.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.config.SpringConfigForIT;
import hexlet.code.dto.LabelDTO;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
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
import java.util.List;

import static hexlet.code.controller.LabelController.ID;
import static hexlet.code.controller.LabelController.LABEL_CONTROLLER_PATH;
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
@ActiveProfiles(SpringConfigForIT.TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfigForIT.class)
public class LabelControllerTest {

    @Autowired
    private TestUtils utils;

    @Autowired
    private TestData data;

    @Autowired
    private LabelRepository repository;

    @BeforeEach
    public void setUp() throws IOException {
        data.saveLabels();
    }

    @AfterEach
    public void tearDown() {
        data.clearAll();
    }

    @Test
    @DisplayName("Get all labels. Available for authorized users.")
    public void testGetAll() throws Exception {
        utils.checkNotAuthorizedRequestIsForbidden(get(LABEL_CONTROLLER_PATH));

        final var response =
                utils.performByUser(get(LABEL_CONTROLLER_PATH), FIRST_USER_MAIL)
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse();
        final List<Label> statuses = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertThat(statuses).hasSize(2);
    }

    @Test
    @DisplayName("Get label by id. Available for authorized users. If label not found get 204 (No Content)")
    public void testGetOne() throws Exception {
        final Label expectedLabel = repository.findAll().get(0);

        utils.checkNotAuthorizedRequestIsForbidden(
                get(LABEL_CONTROLLER_PATH + ID, expectedLabel.getId()));

        utils.performByUser(
                        get(LABEL_CONTROLLER_PATH + ID, -1),
                        FIRST_USER_MAIL)
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();

        final var response = utils
                .performByUser(
                        get(LABEL_CONTROLLER_PATH + ID, expectedLabel.getId()),
                        FIRST_USER_MAIL)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final Label actualLabel = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertEquals(expectedLabel.getId(), actualLabel.getId());
        assertEquals(expectedLabel.getName(), actualLabel.getName());
        assertEquals(0, expectedLabel.getCreatedAt().compareTo(actualLabel.getCreatedAt()));
    }

    @Test
    @DisplayName("Create label. Available for authorized users.")
    public void testCreate() throws Exception {
        LabelDTO newLabel = new LabelDTO("Новая метка");

        utils.checkNotAuthorizedRequestIsForbidden(post(LABEL_CONTROLLER_PATH)
                .content(asJson(newLabel))
                .contentType(APPLICATION_JSON));

        assertEquals(2, repository.count());
        final var response = utils.performByUser(post(LABEL_CONTROLLER_PATH)
                        .content(asJson(newLabel))
                        .contentType(APPLICATION_JSON), FIRST_USER_MAIL)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();
        assertEquals(3, repository.count());
        final Label actualLabel = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertEquals("Новая метка", actualLabel.getName());
    }

    @Test
    @DisplayName("Update label. Available for authorized users.")
    public void testUpdate() throws Exception {
        Label label = repository.findAll().get(0);
        utils.checkNotAuthorizedRequestIsForbidden(
                put(LABEL_CONTROLLER_PATH + ID, label.getId())
                        .content(asJson(label))
                        .contentType(APPLICATION_JSON));
        LabelDTO updatedLabel = new LabelDTO("Updated");
        utils.performByUser(
                        put(LABEL_CONTROLLER_PATH + ID, label.getId())
                                .content(asJson(updatedLabel))
                                .contentType(APPLICATION_JSON), FIRST_USER_MAIL)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final Label actualLabel = repository.findById(label.getId()).orElse(null);
        assertNotNull(actualLabel);
        assertEquals("Updated", actualLabel.getName());
    }

    @Test
    @DisplayName("Delete label.  Available for authorized users.")
    public void testDelete() throws Exception {
        final Label expectedLabel = repository.findAll().get(0);
        Long id  = expectedLabel.getId();
        utils.checkNotAuthorizedRequestIsForbidden(
                delete(LABEL_CONTROLLER_PATH + ID, id));

        assertEquals(2, repository.count());
        utils.performByUser(delete(LABEL_CONTROLLER_PATH + ID, id),
                        FIRST_USER_MAIL)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        assertFalse(repository.existsById(id));
    }
}
