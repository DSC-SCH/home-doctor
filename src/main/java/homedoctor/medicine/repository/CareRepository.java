package homedoctor.medicine.repository;

import homedoctor.medicine.domain.Care;
import homedoctor.medicine.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CareRepository {

    private final EntityManager em;

    public void save(Care care) {
        em.persist(care);
    }

    public Care findOne(Long id) {
        return em.find(Care.class, id);
    }

    public List<Care> findAllByUser(User user) {
        return em.createQuery("select c from Care c where c.user = :user", Care.class)
                .setParameter("user", user)
                .getResultList();
    }
}
