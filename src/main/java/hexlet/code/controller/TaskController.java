package hexlet.code.controller;

import com.querydsl.core.types.Predicate;
import hexlet.code.dto.TaskTO;
import hexlet.code.model.Task;
import hexlet.code.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
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

import static hexlet.code.controller.TaskController.TASKS_CONTROLLER_PATH;


@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + TASKS_CONTROLLER_PATH)
@Tag(name = "Tasks")
public class TaskController {
    public static final String TASKS_CONTROLLER_PATH = "/tasks";

    public static final String ID = "/{id}";

    private TaskService taskService;

    @Operation(summary = "Getting all tasks.")
    @ApiResponses(@ApiResponse(responseCode = "200", content =
    @Content(schema = @Schema(implementation = Task.class))
    ))
    @GetMapping()
    public List<Task> getAll(@QuerydslPredicate(root = Task.class) Predicate predicate) {
        return taskService.getAll(predicate);
    }

    @Operation(summary = "Getting one task.")
    @ApiResponses(@ApiResponse(responseCode = "200", content =
    @Content(schema = @Schema(implementation = Task.class))
    ))
    @GetMapping(ID)
    public Task getOne(@PathVariable final Long id) {
        return taskService.getById(id);
    }

    @Operation(summary = "Creating new task.")
    @ApiResponses(@ApiResponse(responseCode = "201", content =
    @Content(schema = @Schema(implementation = Task.class))
    ))
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@RequestBody TaskTO task) {
        return taskService.create(task);
    }

    @Operation(summary = "Updating task.")
    @ApiResponses(@ApiResponse(responseCode = "200", content =
    @Content(schema = @Schema(implementation = Task.class))
    ))
    @PutMapping(ID)
    @ResponseStatus(HttpStatus.OK)
    public Task update(@PathVariable("id") long id, @RequestBody TaskTO taskTO) {
        return taskService.update(id, taskTO);
    }

    @Operation(summary = "Deleting task.")
    @DeleteMapping(ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final Long id) {
        taskService.delete(id);
    }
}
