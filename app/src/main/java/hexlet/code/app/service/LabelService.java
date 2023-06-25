package hexlet.code.app.service;

import hexlet.code.app.model.Label;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.service.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class LabelService {
    private LabelRepository labelRepository;

    public List<Label> getAll() {
        return labelRepository.findAll();
    }

    public Label getById(Long id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Not found label with id %d", id)));
    }

    public Label create(String name) {
        return labelRepository.save(new Label(null, name, LocalDateTime.now()));
    }

    public Label update(long id, String name) {
        Label label = getById(id);
        label.setName(name);
        return labelRepository.save(label);
    }

    public void delete(Long id) {
        labelRepository.findById(id).ifPresent(labelRepository::delete);
    }
}
