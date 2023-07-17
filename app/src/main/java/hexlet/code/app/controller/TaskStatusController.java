package hexlet.code.app.controller;

import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.service.TaskStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
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
import java.util.Map;

import static hexlet.code.app.controller.TaskStatusController.TASK_STATUS_CONTROLLER_PATH;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + TASK_STATUS_CONTROLLER_PATH)
@Tag(name = "Task Statuses")
public class TaskStatusController {
    public static final String TASK_STATUS_CONTROLLER_PATH = "/statuses";
    public static final String ID = "/{id}";

    private TaskStatusService taskStatusService;

    @Operation(summary = "Getting all task statuses.")
    @ApiResponses(@ApiResponse(responseCode = "200", content =
    @Content(schema = @Schema(implementation = TaskStatus.class))
    ))
    @GetMapping()
    public List<TaskStatus> getAll() {
        return taskStatusService.getAll();
    }

    @Operation(summary = "Getting status by id.")
    @ApiResponses(@ApiResponse(responseCode = "200", content =
    @Content(schema = @Schema(implementation = TaskStatus.class))
    ))
    @GetMapping(ID)
    public TaskStatus getById(@PathVariable final Long id) {
        return taskStatusService.getById(id);
    }

    @Operation(summary = "Creating a new status.")
    @ApiResponses(@ApiResponse(responseCode = "201", content =
    @Content(schema = @Schema(implementation = TaskStatus.class))
    ))
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public TaskStatus create(@RequestBody Map<String, String> map) {
        return taskStatusService.create(map.get("name"));
    }

    @Operation(summary = "Updating status.")
    @ApiResponses(@ApiResponse(responseCode = "200", content =
    @Content(schema = @Schema(implementation = TaskStatus.class))
    ))
    @PutMapping(ID)
    @ResponseStatus(HttpStatus.OK)
    public TaskStatus update(@PathVariable("id") long id, @RequestBody Map<String, String> map) {
        return taskStatusService.update(id, map.get("name"));
    }

    @Operation(summary = "Deleting status.")
    @DeleteMapping(ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final Long id) {
        taskStatusService.delete(id);
    }
}
