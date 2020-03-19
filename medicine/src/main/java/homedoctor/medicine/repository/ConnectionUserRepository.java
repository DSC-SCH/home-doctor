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

    public ConnectionUser findConnectionByUser(Long userId) {
        Long tableCounts = em.createQuery("select count(c) from ConnectionUser c", Long.class)
                .getSingleResult();

        if (tableCounts == 0) {
            return null;
        }

        List<ConnectionUser> connectionUser = em.createQuery("select c from ConnectionUser c join c.user u where u.id = :id",
                ConnectionUser.class)
                .setParameter("id", userId)
                .getResultList();

        if (connectionUser == null || connectionUser.isEmpty()) {
            return null;
        }

        return connectionUser.get(0);
    }

    public void delete(ConnectionUser connectionUser) {
        em.createQuery(
                "delete from ConnectionUser c where c = :connectionUser")
                .setParameter("connectionUser", connectionUser)
                .executeUpdate();
    }

    public List<ConnectionUser> findAllByCareUser(User user) {
        String query = "select c from ConnectionUser c join c.careUser u " +
                "where c.user = :user";

        List<ConnectionUser> userList = em.createQuery(query, ConnectionUser.class)
                .setParameter("user", user)
                .getResultList();
        return userList;
    }

    public List<ConnectionUser> findAllByManagerUser(User user) {
        String query = "select c from ConnectionUser c join c.user u where c.careUser = :user";

        List<ConnectionUser> managerUserList = em.createQuery(query, ConnectionUser.class)
                .setParameter("user", user)
                .getResultList();

        return managerUserList;
    }
}
