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

    public List<PrescriptionImage> findAllByAlarm(Alarm alarm) {
        String query = "select p from PrescriptionImage p join fetch p.alarm where p.alarm = :alarm";

        return em.createQuery(query, PrescriptionImage.class)
                .setParameter("alarm", alarm)
                .getResultList();
    }

    // 명시적 Join 사용하기.
    public List<PrescriptionImage> findAllByUser(User user) {
        String query = "select p from PrescriptionImage p join fetch " +
                "p.alarm join p.alarm a where a.user.id = :id";

        List<PrescriptionImage> prescriptionImages =
                em.createQuery(query,
                        PrescriptionImage.class)
                .setParameter("id", user.getId())
                .getResultList();

        return prescriptionImages;
    }

    public void delete(PrescriptionImage prescriptionImage) {
        em.createQuery(
                "delete from PrescriptionImage p where p.id = :id")
                .setParameter("id", prescriptionImage.getId())
                .executeUpdate();
        em.clear();
    }
}
