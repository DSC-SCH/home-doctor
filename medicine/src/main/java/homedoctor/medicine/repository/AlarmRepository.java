package homedoctor.medicine.repository;

import homedoctor.medicine.domain.Alarm;
import homedoctor.medicine.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
        return em.createQuery("select a from Alarm a join fetch a.label where a.user = :user", Alarm.class)
                .setParameter("user", user)
                .getResultList();
    }

    public List<Alarm> findAllByLabel(Long user, Long label) {

        String query = "select a from Alarm a " +
                "where a.user.id = :user and a.label.id = :label";
        return em.createQuery(query, Alarm.class)
                .setParameter("user", user)
                .setParameter("label", label)
                .getResultList();
    }

    public void updateLabel(Long userId, Long alarmId) {
        em.createQuery("update Alarm a set a.label=1L where a.user.id = :userId and a.id = :alarm")
                .setParameter("userId", userId)
                .setParameter("alarm", alarmId)
                .executeUpdate();
    }

    public List<Alarm> findAllByEnable(User user) {
        String query = "select a from Alarm a join fetch a.label " +
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
    }

    public void deleteByUser(User user) {
        em.createQuery("delete from Alarm a where a.user = :user")
                .setParameter("user", user)
                .executeUpdate();
    }
}

