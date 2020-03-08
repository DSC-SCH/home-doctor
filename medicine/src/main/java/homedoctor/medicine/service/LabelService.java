package homedoctor.medicine.service;

import homedoctor.medicine.domain.Label;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.repository.LabelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class    LabelService {

    private final LabelRepository labelRepository;

    @Transactional
    public Long saveLabel(User user, Label label) {
        label.setUser(user);
        labelRepository.save(label);
        return label.getId();
    }

    public List<Label> fineLabelsByUser(User user) {
        return labelRepository.findAllByUser(user);
    }

    public Label findOne(Long labelId) {
        return labelRepository.findOne(labelId);
    }

    @Transactional
    public void delete(Label label) {
        labelRepository.delete(label);
    }
}
