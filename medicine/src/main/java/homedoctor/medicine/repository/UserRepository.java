package homedoctor.medicine.repository;

import homedoctor.medicine.domain.SnsType;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    private final JwtService jwtService;

    public void save(User user) {
        em.persist(user);
        String setToken = jwtService.create(user.getId());

        user.setToken(setToken);
        log.info(user.getToken());
    }

    public String findUserToken(User user) {
        return em.createQuery("select u.token from User u where u = :user", String.class)
                .setParameter("user", user)
                .getSingleResult();
    }

    public User findOneById(Long id) {
        User user = em.find(User.class, id);
        System.out.println("===user====");
        System.out.println(user.toString());
        return user;
    }

    public User findOneBySnsId(String snsId, SnsType snsType) {
        return em.createQuery("select u from User u where u.snsId = :snsId and u.snsType = :snsType", User.class)
                .setParameter("snsId", snsId)
                .setParameter("snsType", snsType)
                .getSingleResult();
    }

    public List<User> findByEmail(User user) {
        return em.createQuery("select u from User u where u.email = :email", User.class)
                .setParameter("email", user.getEmail())
                .getResultList();
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

    public void deleteByUserId(User user) {
        em.createQuery("delete from User u where u.id = :id")
                .setParameter("id", user.getId())
                .executeUpdate();
        em.clear();
    }
}
