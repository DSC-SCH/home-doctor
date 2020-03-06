package homedoctor.medicine.repository;

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
        return em.find(PrescriptionImage.class, id);
    }

    public List<PrescriptionImage> findAllByUser(User user) {
        return em.createQuery("select p from PrescriptionImage p where p.user = :user", PrescriptionImage.class)
                .setParameter("user", user)
                .getResultList();
    }
}
