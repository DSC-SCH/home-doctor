package homedoctor.medicine.repository;

import homedoctor.medicine.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public void save(User user) {
        em.persist(user);
    }

    public User findOne(Long id) {
        return em.find(User.class, id);
    }

    public List<User> findAll() {
        return em.createQuery("select u from User u", User.class)
                .getResultList();
    }

    public List<User> findByName(String name) {
        return em.createQuery("select u from User u where u.username = :name", User.class)
                .setParameter("name", name)
                .getResultList();

    }

    public void deleteByUser(User user) {
        em.createQuery("select u from User u where u.id = :id", User.class)
                .setParameter("id", user.getId())
                .executeUpdate();
    }
}
