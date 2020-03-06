package homedoctor.medicine.service;

import homedoctor.medicine.domain.Care;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.repository.CareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CareService {

    private final CareRepository careRepository;

    @Transactional
    public Long save(User user, User careUser, Care care) {
        care.setUser(user);
        care.setUser(user);
        careRepository.save(care);

        return care.getId();
    }

    public Care findOne(Long careId) {
        return careRepository.findOne(careId);
    }

    public List<Care> findByCareUsers(User user) {
        return careRepository.findAllByUser(user);
    }
}
