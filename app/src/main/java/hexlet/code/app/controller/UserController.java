package hexlet.code.app.controller;

import hexlet.code.app.dto.UserTO;
import hexlet.code.app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import java.util.Map;

import static hexlet.code.app.controller.UserController.USER_CONTROLLER_PATH;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + USER_CONTROLLER_PATH)
public class UserController {
    public static final String USER_CONTROLLER_PATH = "/users";
    public static final String ID = "/{id}";
    private final UserService userService;

    @GetMapping()
    public List<UserTO> getUserById() {
        return userService.getAll();
    }

    @GetMapping(ID)
    public UserTO getUserById(@PathVariable final Long id) {
        return userService.getById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Object createUser(@RequestBody Map<String, String> map) {
        UserTO user = userService.create(map.get("firstName"),
                map.get("lastName"),
                map.get("email"),
                map.get("password"));
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        return user;
    }

    @PutMapping(ID)
    @ResponseStatus(HttpStatus.OK)
    public Object updateUser(@RequestBody Map<String, String> map, @PathVariable final Long id) {
        UserTO user = userService.update(id, map.get("firstName"),
                map.get("lastName"),
                map.get("email"),
                map.get("password"));
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        return user;
    }

    @DeleteMapping(ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@RequestBody Map<String, String> map, @PathVariable final Long id) {
        userService.delete(id);
    }
}
