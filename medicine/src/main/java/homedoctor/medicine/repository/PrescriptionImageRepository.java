package homedoctor.medicine.repository;

import homedoctor.medicine.domain.Alarm;
import homedoctor.medicine.domain.PrescriptionImage;
import homedoctor.medicine.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PrescriptionImageRepository {

    private final EntityManager em;

    public void save(PrescriptionImage image) {
        em.persist(image);
    }

    public PrescriptionImage findOne(Long id) {
        String query = "select p from PrescriptionImage p join fetch p.alarm where p.id = :id";
        return em.createQuery(query, PrescriptionImage.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public List<PrescriptionImage> findAllByAlarm(Long alarmId) {
        String query = "select p from PrescriptionImage p join fetch p.alarm where p.alarm.id = :alarm";

        return em.createQuery(query, PrescriptionImage.class)
                .setParameter("alarm", alarmId)
                .getResultList();
    }

    // 명시적 Join 사용하기.
    public List<PrescriptionImage> findAllByUser(Long userId) {
        String query = "select p from PrescriptionImage p join fetch " +
                "p.alarm join p.alarm a where a.user.id = :id";

        List<PrescriptionImage> prescriptionImages =
                em.createQuery(query,
                        PrescriptionImage.class)
                .setParameter("id", userId)
                .getResultList();

        return prescriptionImages;
    }

    public void delete(Long imageId) {
        em.createQuery(
                "delete from PrescriptionImage p where p.id = :id")
                .setParameter("id", imageId)
                .executeUpdate();
        em.clear();
    }
}
