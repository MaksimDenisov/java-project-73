package hexlet.code.app.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.app.config.SpringConfigForIT;
import hexlet.code.app.dto.UserTO;
import hexlet.code.app.model.User;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.List;

import static hexlet.code.app.config.SpringConfigForIT.TEST_PROFILE;
import static hexlet.code.app.controller.UserController.ID;
import static hexlet.code.app.controller.UserController.USER_CONTROLLER_PATH;
import static hexlet.code.app.utils.TestUtils.USER_MAIL;
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
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfigForIT.class)
public class UserControllerTest {
    @Autowired
    private TestUtils utils;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() throws IOException {
        utils.registerUsers();
    }

    @AfterEach
    public void tearDown() {
        utils.tearDown();
    }

    @Test
    @DisplayName("Test data exist")
    public void shouldTestDataExist() throws IOException {
        assertEquals(2, userRepository.count());
    }

    @Test
    @DisplayName("Get all users.")
    public void shouldGetAllUsers() throws Exception {
        final var response = utils.perform(get(USER_CONTROLLER_PATH))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final List<User> users = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertThat(users).hasSize(2);
    }

    @Test
    @DisplayName("Should save user.")
    public void shouldCreateUser() throws Exception {
        assertEquals(2, userRepository.count());
        final var response = utils.perform(post(USER_CONTROLLER_PATH)
                        .content(asJson(new UserTO("new@mail.com", "New", "New", "new_pass")))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();
        assertEquals(3, userRepository.count());
        final User user = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertEquals("new@mail.com", user.getEmail());
        assertEquals("New", user.getFirstName());
        assertEquals("New", user.getLastName());
    }

    @Test
    @DisplayName("If the request contains invalid data, a response with the status code 422 should be returned.")
    public void shouldNotCreateIncorrectUser() throws Exception {
        utils.perform(post(USER_CONTROLLER_PATH)
                        .content(asJson(new UserTO("new@mail.com", "", "New", "new_pass")))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
        utils.perform(post(USER_CONTROLLER_PATH)
                        .content(asJson(new UserTO("new@mail.com", "New", "", "")))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Get user by id")
    public void shouldGetUserById() throws Exception {
        final User expectedUser = userRepository.findAll().get(0);
        final var response = utils.perform(get(USER_CONTROLLER_PATH + ID, expectedUser.getId()), USER_MAIL)
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
    @DisplayName("Unauthorized get user by id.")
    public void shouldGetUserByIdUnauthorized() throws Exception {
        final User expectedUser = userRepository.findAll().get(0);
        utils.perform(get(USER_CONTROLLER_PATH + ID, expectedUser.getId()))
                .andExpect(status().isForbidden())
                .andReturn()
                .getResponse();
    }

    @Test
    @DisplayName("Should update user.")
    public void shouldUpdateUser() throws Exception {
        final User expectedUser = userRepository.findAll().get(0);
        final var response = utils.perform(put(USER_CONTROLLER_PATH + ID, expectedUser.getId())
                        .content(asJson(new UserTO("new@mail.com", "New", "New", "new_pass")))
                        .contentType(APPLICATION_JSON), USER_MAIL)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final User user = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        final User actualUser = userRepository.findById(expectedUser.getId()).get();
        assertEquals("new@mail.com", user.getEmail());
        assertEquals("New", user.getFirstName());
        assertEquals("New", user.getLastName());

        assertEquals("new@mail.com", actualUser.getEmail());
        assertEquals("New", actualUser.getFirstName());
        assertEquals("New", actualUser.getLastName());
    }

    @Test
    @DisplayName("Unauthorized updating user.")
    public void shouldNotUpdateUser() throws Exception {
        final User expectedUser = userRepository.findAll().get(1);
        utils.perform(put(USER_CONTROLLER_PATH + ID, expectedUser.getId(), USER_MAIL)
                        .content(asJson(new UserTO("new@mail.com", "New", "New", "new_pass")))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn()
                .getResponse();
        utils.perform(put(USER_CONTROLLER_PATH + ID, expectedUser.getId())
                        .content(asJson(new UserTO("new@mail.com", "New", "New", "new_pass")))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn()
                .getResponse();
    }

    @Test
    @DisplayName("Delete user.")
    public void shouldDeleteUser() throws Exception {
        assertEquals(2, userRepository.count());
        final User expectedUser = userRepository.findAll().get(0);
        utils.perform(delete(USER_CONTROLLER_PATH + ID, expectedUser.getId()), USER_MAIL)
                .andExpect(status().isNoContent())
                .andReturn()
                .getResponse();
        assertEquals(1, userRepository.count());
    }

    @Test
    @DisplayName("Unauthorized delete user.")
    public void shouldNotDeleteUser() throws Exception {
        assertEquals(2, userRepository.count());
        final User expectedUser = userRepository.findAll().get(1);
        utils.perform(delete(USER_CONTROLLER_PATH + ID, expectedUser.getId()))
                .andExpect(status().isForbidden())
                .andReturn()
                .getResponse();
        utils.perform(delete(USER_CONTROLLER_PATH + ID, expectedUser.getId()), USER_MAIL)
                .andExpect(status().isForbidden())
                .andReturn()
                .getResponse();
    }
}
