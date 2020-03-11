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

    public List<Alarm> findAllByEnable(User user) {
        String query = "select a from Alarm a " +
                "where a.user = :user and " +
                "a.alarmStatus = homedoctor.medicine.domain.AlarmStatus.ENABLE";

        List<Alarm> enableAlarmList = em.createQuery(query, Alarm.class)
                .setParameter("user", user)
                .getResultList();

        return enableAlarmList;
    }

    public void delete(Alarm alarm) {
        em.createQuery(
                "delete from Alarm a where a.id = :id")
                .setParameter("id", alarm.getId())
                .executeUpdate();
        em.clear();
    }
}

