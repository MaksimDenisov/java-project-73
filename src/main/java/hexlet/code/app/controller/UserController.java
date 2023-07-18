package hexlet.code.app.controller;

import hexlet.code.app.dto.UserTO;
import hexlet.code.app.model.User;
import hexlet.code.app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static hexlet.code.app.controller.UserController.USER_CONTROLLER_PATH;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + USER_CONTROLLER_PATH)
@Tag(name = "User")
public class UserController {
    public static final String USER_CONTROLLER_PATH = "/users";
    public static final String ID = "/{id}";

    private static final String ONLY_OWNER_BY_ID = """
            @userRepository.findById(#id).get().getEmail() == authentication.getName()
        """;

    private final UserService userService;

    @Operation(summary = "Getting all users.")
    @ApiResponses(@ApiResponse(responseCode = "200", content =
        @Content(schema = @Schema(implementation = User.class))
    ))
    @GetMapping()
    public List<User> getAll() {
        return userService.getAll();
    }

    @Operation(summary = "Getting one user.")
    @ApiResponses(@ApiResponse(responseCode = "200", content =
                    @Content(schema = @Schema(implementation = User.class))
    ))
    @GetMapping(ID)
    public User getOne(@PathVariable final Long id) {
        return userService.getById(id);
    }

    @Operation(summary = "Creating user.")
    @ApiResponses(@ApiResponse(responseCode = "200", content =
    @Content(schema = @Schema(implementation = User.class))
    ))
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody @Valid UserTO user) {
        return userService.create(user);
    }


    @Operation(summary = "Updating user.")
    @ApiResponses(@ApiResponse(responseCode = "200", content =
    @Content(schema = @Schema(implementation = User.class))
    ))
    @PutMapping(ID)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(ONLY_OWNER_BY_ID)
    public User updateUser(@RequestBody @Valid UserTO user, @PathVariable final Long id) {
        return userService.update(id, user);
    }

    @Operation(summary = "Deleting user.")
    @DeleteMapping(ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize(ONLY_OWNER_BY_ID)
    public void deleteUser(@PathVariable final Long id) {
        userService.delete(id);
    }
}
