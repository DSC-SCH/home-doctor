package homedoctor.medicine.repository;

import homedoctor.medicine.domain.Medicine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MedicineRepository {

    private final EntityManager em;

    public List<Medicine> findAllByName(String name) {
        return em.createQuery("select m from Medicine m where m.name = :name", Medicine.class)
                .setParameter("name", name)
                .getResultList();
    }

    public List<Medicine> findAllByEffect(String effect) {
        return em.createQuery("select m from Medicine m where m.effect = :effect", Medicine.class)
                .setParameter("effect", effect)
                .getResultList();
    }
}
