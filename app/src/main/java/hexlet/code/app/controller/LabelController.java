package hexlet.code.app.controller;

import hexlet.code.app.model.Label;
import hexlet.code.app.service.LabelService;
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
public class LabelController {
    public static final String LABEL_CONTROLLER_PATH = "/labels";

    public static final String ID = "/{id}";


    private LabelService labelService;

    @GetMapping()
    public List<Label> getAll() {
        return labelService.getAll();
    }

    @GetMapping(ID)
    public Label getById(@PathVariable final Long id) {
        return labelService.getById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Label create(@RequestBody Map<String, String> map) {
        return labelService.create(map.get("name"));
    }

    @PutMapping(ID)
    @ResponseStatus(HttpStatus.OK)
    public Label update(@PathVariable("id") long id, @RequestBody Map<String, String> map) {
        return labelService.update(id, map.get("name"));
    }

    @DeleteMapping(ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final Long id) {
        labelService.delete(id);
    }
}
