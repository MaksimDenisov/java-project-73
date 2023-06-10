package hexlet.code.app.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.app.config.SpringConfigForIT;
import hexlet.code.app.dto.UserTO;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
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

import java.time.LocalDateTime;
import java.util.List;

import static hexlet.code.app.config.SpringConfigForIT.TEST_PROFILE;
import static hexlet.code.app.controller.UserController.ID;
import static hexlet.code.app.controller.UserController.USER_CONTROLLER_PATH;
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

    private static final User USER_1 = new User(null,
            "Ivan", "Ivanov",
            "ivanov@mail.com", "ivanov_pass",
            LocalDateTime.of(2023, 6, 10, 9, 0));
    private static final User USER_2 = new User(null,
            "Petr", "Petrov",
            "petrov@mail.com", "petrov_pass",
            LocalDateTime.of(2023, 6, 10, 10, 0));
    private static final User NEW_USER = new User(null,
            "New", "New",
            "new@mail.com", "new_pass",
            LocalDateTime.of(2023, 6, 10, 11, 0));

    private static final UserTO NEW_DTO = new UserTO(null, "new@mail.com", "New", "New",
            LocalDateTime.of(2023, 6, 10, 11, 0));

    private static final String NEW_USER_TO = "{\n"
            + "    \"email\": \"new@mail.com\",\n"
            + "    \"firstName\": \"New\",\n"
            + "    \"lastName\": \"New\",\n"
            + "    \"password\": \"new_pass\"\n"
            + "}";

    private static final String INCORRECT_USER_TO = "{\n"
            + "    \"email\": \"new@mail.com\",\n"
            + "    \"firstName\": \"\",\n"
            + "    \"lastName\": \"New\",\n"
            + "    \"password\": \"new_pass\"\n"
            + "}";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.save(USER_1);
        userRepository.save(USER_2);
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Get user by id")
    public void shouldGetUserById() throws Exception {
        final User expectedUser = userRepository.findAll().get(0);
        final var response = mockMvc.perform(get(USER_CONTROLLER_PATH + ID, expectedUser.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final UserTO userTO = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertEquals(expectedUser.getId(), userTO.getId());
        assertEquals(expectedUser.getEmail(), userTO.getEmail());
        assertEquals(expectedUser.getFirstName(), userTO.getFirstName());
        assertEquals(expectedUser.getLastName(), userTO.getLastName());
    }

    @Test
    @DisplayName("Get all users.")
    public void shouldGetAllUsers() throws Exception {
        final var response = mockMvc.perform(get(USER_CONTROLLER_PATH))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final List<UserTO> users = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertThat(users).hasSize(2);
    }

    @Test
    @DisplayName("Should save user.")
    public void shouldCreateUser() throws Exception {
        assertEquals(2, userRepository.count());
        final var response = mockMvc.perform(post(USER_CONTROLLER_PATH)
                        .content(NEW_USER_TO)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();
        assertEquals(3, userRepository.count());
        final UserTO userTO = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertEquals("new@mail.com", userTO.getEmail());
        assertEquals("New", userTO.getFirstName());
        assertEquals("New", userTO.getLastName());
    }

    @Test
    @DisplayName("If the request contains invalid data, a response with the status code 422 should be returned.")
    public void shouldNotCreateIncorrectUser() throws Exception {
        mockMvc.perform(post(USER_CONTROLLER_PATH)
                        .content(INCORRECT_USER_TO)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Should update user.")
    public void shouldUpdateUser() throws Exception {
        final User expectedUser = userRepository.findAll().get(0);
        final var response = mockMvc.perform(put(USER_CONTROLLER_PATH + ID, expectedUser.getId())
                        .content(NEW_USER_TO)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final UserTO userTO = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        final User actualUser = userRepository.findById(expectedUser.getId()).get();
        assertEquals("new@mail.com", userTO.getEmail());
        assertEquals("New", userTO.getFirstName());
        assertEquals("New", userTO.getLastName());

        assertEquals("new@mail.com", actualUser.getEmail());
        assertEquals("New", actualUser.getFirstName());
        assertEquals("New", actualUser.getLastName());
    }

    @Test
    @DisplayName("Should delete user.")
    public void shouldDeleteUser() throws Exception {
        assertEquals(2, userRepository.count());
        final User expectedUser = userRepository.findAll().get(0);
        mockMvc.perform(delete(USER_CONTROLLER_PATH + ID, expectedUser.getId())
                        .content(NEW_USER_TO)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn()
                .getResponse();
        assertEquals(1, userRepository.count());
    }
}
