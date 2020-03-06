package homedoctor.medicine.service;

import homedoctor.medicine.domain.PrescriptionImage;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.repository.PrescriptionImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrescriptionImageService {

    private final PrescriptionImageRepository prescriptionImageRepository;

    @Transactional
    public Long save(User user, PrescriptionImage image) {
        image.setUser(user);
        prescriptionImageRepository.save(image);
        return image.getId();
    }

    public List<PrescriptionImage> findImagesByUser(User user) {
        return prescriptionImageRepository.findAllByUser(user);
    }
}
