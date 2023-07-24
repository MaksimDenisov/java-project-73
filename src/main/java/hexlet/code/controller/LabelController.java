package hexlet.code.controller;

import hexlet.code.dto.LabelDTO;
import hexlet.code.model.Label;
import hexlet.code.service.LabelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

import static hexlet.code.controller.LabelController.LABEL_CONTROLLER_PATH;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + LABEL_CONTROLLER_PATH)
@Tag(name = "Labels")
public class LabelController {
    public static final String LABEL_CONTROLLER_PATH = "/labels";

    public static final String ID = "/{id}";

    private final  LabelService labelService;

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
    public Label create(@RequestBody @Valid LabelDTO labelDTO) {
        return labelService.create(labelDTO.getName());
    }

    @Operation(summary = "Updating new label.")
    @ApiResponses(@ApiResponse(responseCode = "200", content =
    @Content(schema = @Schema(implementation = Label.class))
    ))
    @PutMapping(ID)
    @ResponseStatus(HttpStatus.OK)
    public Label update(@PathVariable("id") long id, @Valid @RequestBody LabelDTO labelDTO) {
        return labelService.update(id, labelDTO.getName());
    }

    @Operation(summary = "Deleting label.")
    @DeleteMapping(ID)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable final Long id) {
        labelService.delete(id);
    }
}
