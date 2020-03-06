package homedoctor.medicine.service;

import homedoctor.medicine.domain.ConnectCode;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.repository.ConnectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConnectService {

    private final ConnectRepository connectRepository;

    @Transactional
    public Long saveCode(User user, ConnectCode code) {
        code.setUser(user);
        connectRepository.save(code);
        return code.getId();
    }

    public ConnectCode findCode(Long codeId) {
        return connectRepository.findOne(codeId);
    }
}
