package homedoctor.medicine.repository;

import homedoctor.medicine.domain.Alarm;
import homedoctor.medicine.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AlarmRepository {

    private final EntityManager em;

    public void save(Alarm alarm) {
        em.persist(alarm);
    }

    public Alarm findOne(Long id) {
        return em.find(Alarm.class, id);
    }

    public List<Alarm> findAllByUser(User user) {
        return em.createQuery("select a from Alarm a where a.user = :user", Alarm.class)
                .setParameter("user", user)
                .getResultList();
    }
}
