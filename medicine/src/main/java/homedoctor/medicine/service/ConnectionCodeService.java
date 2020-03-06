package homedoctor.medicine.service;

import homedoctor.medicine.domain.ConnectionCode;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.repository.ConnectionCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConnectionCodeService {

    private final ConnectionCodeRepository connectionCodeRepository;

    @Transactional
    public Long saveCode(User user, ConnectionCode code) {
        code.setUser(user);
        connectionCodeRepository.save(code);
        return code.getId();
    }

    public ConnectionCode findCode(Long codeId) {
        return connectionCodeRepository.findOne(codeId);
    }
}
