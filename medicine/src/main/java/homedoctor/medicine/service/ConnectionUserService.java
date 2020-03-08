package homedoctor.medicine.service;

import homedoctor.medicine.domain.ConnectionUser;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.repository.ConnectionUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ConnectionUserService {

    // 의존성 주입은 어노테이션이 해줌
    private ConnectionUserRepository connectionUserRepository;

    @Transactional
    public Long save(User managerUser, User receiverUser) {
        ConnectionUser connectionUser = ConnectionUser.createConnection(managerUser, receiverUser);
        System.out.println("=====================");
        System.out.println("Care User : " + connectionUser.getCareUser().getId());
        System.out.println("manage User : " + connectionUser.getUser().getId());

        connectionUserRepository.save(connectionUser);

        return connectionUser.getId();
    }

    @Transactional
    public void delete(ConnectionUser connectionUser) {
        connectionUserRepository.delete(connectionUser);
    }

    public List<User> findAllReceiverByUser(User user) {
        return connectionUserRepository.findAllByCareUser(user);
    }

    public List<User> findALlManagerByUser(User user) {
        return connectionUserRepository.findAllByManagerUser(user);
    }

}
