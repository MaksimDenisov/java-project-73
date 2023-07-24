package hexlet.code.service;

import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.service.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class LabelService {
    private final LabelRepository labelRepository;

    public List<Label> getAll() {
        return labelRepository.findAll();
    }

    public Set<Label> getByIds(List<Long> ids) {
        return new HashSet<>(labelRepository.findAllById(ids));
    }

    public Label getById(Long id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Not found label with id %d", id)));
    }

    public Label create(String name) {
        return labelRepository.save(new Label(name));
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
