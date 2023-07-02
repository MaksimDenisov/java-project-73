package hexlet.code.app.controller;

import hexlet.code.app.dto.TaskTO;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.User;
import hexlet.code.app.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.querydsl.core.types.Predicate;

import java.util.List;

import static hexlet.code.app.controller.TaskController.TASKS_CONTROLLER_PATH;


@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + TASKS_CONTROLLER_PATH)
public class TaskController {
    public static final String TASKS_CONTROLLER_PATH = "/tasks";

    public static final String ID = "/{id}";

    private TaskService taskService;

    @GetMapping()
    public List<Task> getAll(@QuerydslPredicate(root = Task.class) Predicate predicate) {
        return taskService.getAll(predicate);
    }

    @GetMapping(ID)
    public Task getById(@PathVariable final Long id) {
        return taskService.getById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@RequestBody TaskTO task) {
        return taskService.create(task);
    }

    @PutMapping(ID)
    @ResponseStatus(HttpStatus.OK)
    public Task update(@PathVariable("id") long id, @RequestBody TaskTO taskTO) {
        return taskService.update(id, taskTO);
    }

    @DeleteMapping(ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final Long id) {
        taskService.delete(id);
    }
}
