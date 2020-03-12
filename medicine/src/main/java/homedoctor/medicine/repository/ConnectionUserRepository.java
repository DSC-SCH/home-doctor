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

    public ConnectionUser findConnection(Long id) {
        return em.find(ConnectionUser.class, id);
    }

    public void delete(ConnectionUser connectionUser) {
        em.createQuery(
                "delete from ConnectionUser c where c = :connectionUser")
                .setParameter("connectionUser", connectionUser)
                .executeUpdate();
    }

    public List<User> findAllByCareUser(User user) {
        String query = "select c.careUser from ConnectionUser c join c.careUser u " +
                "where c.user = :user";

        List<User> userList = em.createQuery(query, User.class)
                .setParameter("user", user)
                .getResultList();
        return userList;
    }

    public List<User> findAllByManagerUser(User user) {
        String query = "select c.user from ConnectionUser c join c.user u where c.careUser = :user";

        List<User> managerUserList = em.createQuery(query, User.class)
                .setParameter("user", user)
                .getResultList();

        return managerUserList;
    }
}
