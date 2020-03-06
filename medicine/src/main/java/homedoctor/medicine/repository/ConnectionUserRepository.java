package homedoctor.medicine.repository;

import homedoctor.medicine.domain.ConnectionUser;
import homedoctor.medicine.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConnectionUserRepository {

    private final EntityManager em;

    public void save(ConnectionUser connectionUser) {
        em.persist(connectionUser);
    }

    public ConnectionUser findOne(Long id) {
        return em.find(ConnectionUser.class, id);
    }

    public List<ConnectionUser> findAllCareUser(User user) {
        return em.createQuery("select c from ConnectionUser c where c.user = :user", ConnectionUser.class)
                .setParameter("user", user)
                .getResultList();
    }
}
