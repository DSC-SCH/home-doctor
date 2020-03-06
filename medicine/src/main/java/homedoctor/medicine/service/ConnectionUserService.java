package homedoctor.medicine.service;

import homedoctor.medicine.domain.ConnectionUser;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.repository.ConnectionUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConnectionUserService {

    private final ConnectionUserRepository connectionUserRepository;

    @Transactional
    public Long save(User user, User careUser) {
        ConnectionUser connectionUser = ConnectionUser.createConnectionUser(user, careUser);
        connectionUserRepository.save(connectionUser);

        return connectionUser.getId();
    }

    public ConnectionUser findOne(Long careId) {
        return connectionUserRepository.findOne(careId);
    }

    public List<ConnectionUser> findAllCareUsers(User user) {
        return connectionUserRepository.findAllCareUser(user);
    }
}
