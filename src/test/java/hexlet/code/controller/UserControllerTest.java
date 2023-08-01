package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.config.SpringConfigForIT;
import hexlet.code.dto.UserDTO;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
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

import static hexlet.code.controller.UserController.ID;
import static hexlet.code.controller.UserController.USER_CONTROLLER_PATH;
import static hexlet.code.utils.TestData.FIRST_USER_MAIL;
import static hexlet.code.utils.TestData.SECOND_USER_MAIL;
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
public class UserControllerTest {
    @Autowired
    private TestUtils utils;

    @Autowired
    private TestData data;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() throws IOException {
        data.registerUsers();
    }

    @AfterEach
    public void tearDown() {
        data.clearAll();
    }

    @Test
    @DisplayName("Get all users. Available for all users.")
    public void testGetAll() throws Exception {
        final var response = utils.perform(get(USER_CONTROLLER_PATH))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final List<User> users = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertThat(users).hasSize((int) userRepository.count());
    }

    @Test
    @DisplayName("Get user by id. Available for all users.")
    public void testGetOne() throws Exception {
        final User expectedUser = userRepository.findAll().get(0);

        utils.checkNotAuthorizedRequestIsForbidden(get(USER_CONTROLLER_PATH + ID, expectedUser.getId()));

        final var response = utils.performByUser(
                        get(USER_CONTROLLER_PATH + ID, expectedUser.getId()), FIRST_USER_MAIL)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final User user = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertEquals(expectedUser.getId(), user.getId());
        assertEquals(expectedUser.getEmail(), user.getEmail());
        assertEquals(expectedUser.getFirstName(), user.getFirstName());
        assertEquals(expectedUser.getLastName(), user.getLastName());
    }

    @Test
    @DisplayName("Create user. Available for all users.")
    public void testCreate() throws Exception {
        long countBeforeOperation = userRepository.count();

        final var response = utils.perform(
                        post(USER_CONTROLLER_PATH)
                                .content(asJson(
                                        new UserDTO("new@mail.com", "New",
                                                "New", "new_pass")))
                                .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();
        assertEquals(countBeforeOperation + 1, userRepository.count());
        final User resultUser = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        final User actualUser = userRepository.findById(resultUser.getId()).orElse(null);
        assertNotNull(actualUser);
        assertEquals("new@mail.com", actualUser.getEmail());
        assertEquals("New", actualUser.getFirstName());
        assertEquals("New", actualUser.getLastName());
    }

    @Test
    @DisplayName("If the request contains invalid data, a response with the status code 422 should be returned.")
    public void testCreateIncorrectUser() throws Exception {
        utils.perform(post(USER_CONTROLLER_PATH)
                        .content(asJson(new UserDTO("new@mail.com", "",
                                "New", "new_pass")))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
        utils.perform(post(USER_CONTROLLER_PATH)
                        .content(asJson(new UserDTO("new@mail.com", "New",
                                "", "")))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Update user. Available for owner.")
    public void testUpdate() throws Exception {
        final User anotherUser = userRepository.findAll().get(1);
        utils.perform(put(USER_CONTROLLER_PATH + ID, anotherUser.getId(), FIRST_USER_MAIL)
                        .content(asJson(new UserDTO("new@mail.com", "New",
                                "New", "new_pass")))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn()
                .getResponse();

        utils.checkNotAuthorizedRequestIsForbidden(put(USER_CONTROLLER_PATH + ID, anotherUser.getId())
                .content(asJson(new UserDTO("new@mail.com", "New", "New", "new_pass")))
                .contentType(APPLICATION_JSON));

        final User expectedUser = userRepository.findAll().get(0);

        final var response = utils.performByUser(
                        put(USER_CONTROLLER_PATH + ID, expectedUser.getId())
                                .content(asJson(new UserDTO("new@mail.com", "New",
                                        "New", "new_pass")))
                                .contentType(APPLICATION_JSON), FIRST_USER_MAIL)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final User user = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        final User actualUser = userRepository.findById(expectedUser.getId()).orElse(null);
        assertEquals("new@mail.com", user.getEmail());
        assertEquals("New", user.getFirstName());
        assertEquals("New", user.getLastName());

        assertNotNull(actualUser);
        assertEquals("new@mail.com", actualUser.getEmail());
        assertEquals("New", actualUser.getFirstName());
        assertEquals("New", actualUser.getLastName());
    }

    @Test
    @DisplayName("Delete user. Available for owner.")
    public void testDelete() throws Exception {
        long countBeforeOperation = userRepository.count();

        final User anotherUser = userRepository.findAll().get(1);
        utils.performByUser(delete(USER_CONTROLLER_PATH + ID, anotherUser.getId()), FIRST_USER_MAIL)
                .andExpect(status().isForbidden())
                .andReturn()
                .getResponse();

        final long id = userRepository.findAll().get(0).getId();

        utils.checkNotAuthorizedRequestIsForbidden(delete(USER_CONTROLLER_PATH + ID, id));

        utils.performByUser(delete(USER_CONTROLLER_PATH + ID, id), SECOND_USER_MAIL)
                .andExpect(status().isForbidden())
                .andReturn()
                .getResponse();

        utils.performByUser(delete(USER_CONTROLLER_PATH + ID, id), FIRST_USER_MAIL)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertEquals(countBeforeOperation - 1, userRepository.count());
        assertFalse(userRepository.existsById(id));
    }
}
