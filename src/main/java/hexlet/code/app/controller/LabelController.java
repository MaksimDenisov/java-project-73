package hexlet.code.app.controller;

import hexlet.code.app.model.Label;
import hexlet.code.app.service.LabelService;
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

import static hexlet.code.app.controller.LabelController.LABEL_CONTROLLER_PATH;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + LABEL_CONTROLLER_PATH)
@Tag(name = "Labels")
public class LabelController {
    public static final String LABEL_CONTROLLER_PATH = "/labels";

    public static final String ID = "/{id}";

    private LabelService labelService;

    @Operation(summary = "Getting all labels.")
    @ApiResponses(@ApiResponse(responseCode = "200", content =
    @Content(schema = @Schema(implementation = Label.class))
    ))
    @GetMapping()
    public List<Label> getAll() {
        return labelService.getAll();
    }

    @Operation(summary = "Getting one label.")
    @ApiResponses(@ApiResponse(responseCode = "200", content =
    @Content(schema = @Schema(implementation = Label.class))
    ))
    @GetMapping(ID)
    public Label getById(@PathVariable final Long id) {
        return labelService.getById(id);
    }

    @Operation(summary = "Creating new label.")
    @ApiResponses(@ApiResponse(responseCode = "201", content =
    @Content(schema = @Schema(implementation = Label.class))
    ))
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Label create(@RequestBody Map<String, String> map) {
        return labelService.create(map.get("name"));
    }

    @Operation(summary = "Updating new label.")
    @ApiResponses(@ApiResponse(responseCode = "200", content =
    @Content(schema = @Schema(implementation = Label.class))
    ))
    @PutMapping(ID)
    @ResponseStatus(HttpStatus.OK)
    public Label update(@PathVariable("id") long id, @RequestBody Map<String, String> map) {
        return labelService.update(id, map.get("name"));
    }

    @Operation(summary = "Deleting label.")
    @DeleteMapping(ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final Long id) {
        labelService.delete(id);
    }
}
