package homedoctor.medicine.repository;


import homedoctor.medicine.domain.Alarm;
import homedoctor.medicine.domain.AlarmCount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AlarmCountRepository {

    private final EntityManager em;


    public void save(AlarmCount alarmCount) {
        em.persist(alarmCount);
    }

    public AlarmCount findOne(Long alarmCountId) {
        return em.find(AlarmCount.class, alarmCountId);
    }

    public List<AlarmCount> findAlarmCountsByAlarmId(Long alarmId) {
        return em.createQuery("select a from AlarmCount a " +
                "where a.alarmId.id = :alarmId", AlarmCount.class)
                .setParameter("alarmId", alarmId)
                .getResultList();
    }

    public AlarmCount findOneByAlarmDate(Long alarmId, String date) {
        // 두개 이상일때 장애. -> 장애 대처할 수 있는 코드나 방지법 고민
       return em.createQuery("select a from AlarmCount a" +
                " where a.alarmId.id = :alarmId and a.alarmDate = :date", AlarmCount.class)
                .setParameter("alarmId", alarmId)
                .setParameter("date", date)
                .getSingleResult();
    }

    public List<AlarmCount> findAllByDate(String date) {
        return em.createQuery("select a from AlarmCount a where " +
                "a.alarmDate = :date", AlarmCount.class)
                .setParameter("date", date)
                .getResultList();
    }

    // 특정 알람의 모든 날짜에 대한 횟수
    public List<AlarmCount> findAllByAlarm(Long alarmId) {
        return em.createQuery("select a from AlarmCount a where " +
                "a.alarmId.id = :alarmId", AlarmCount.class)
                .setParameter("alarmId", alarmId)
                .getResultList();
    }

    // 유저의 특정 날짜의 모든 알람에 대한 횟수 가져오기
    public List<AlarmCount> findAllByUserDate(Long userId, String date) {
        return em.createQuery("select a from AlarmCount a where " +
                "a.userId.id = :userId and a.alarmDate = :date", AlarmCount.class)
                .setParameter("userId", userId)
                .setParameter("date", date)
                .getResultList();
    }


    // 특정 alarm, 날짜에서 약을 복용했을시 복용 횟수 수정.
    public void changeAlarmCountDate(Long alarmCountId, String date) {
        em.createQuery("update AlarmCount a set a.alarmDate = :date " +
                "where a.alarmId.id = :alarmId")
                .executeUpdate();
    }

    public void deleteAllAlarmCountByAlarm(Long alarmId) {
        em.createQuery("delete from AlarmCount a where a.alarmId.id = :alarmId")
                .setParameter("alarmId", alarmId)
                .executeUpdate();
    }
}
