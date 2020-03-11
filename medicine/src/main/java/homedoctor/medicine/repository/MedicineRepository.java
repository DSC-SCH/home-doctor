package homedoctor.medicine.repository;

import homedoctor.medicine.domain.Medicine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MedicineRepository {

    private final EntityManager em;

    public Medicine findOneMedicine(Long medicineId) {
        return em.createQuery("select m from Medicine m where m.id = :id", Medicine.class)
                .setParameter("id", medicineId)
                .getSingleResult();
    }

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

    public List<Medicine> findAllByKeyword(String keyword) {
        List<Medicine> nameSearch = em.createQuery("select m from Medicine m " +
                "where m.name like :keyword", Medicine.class)
                .setParameter("keyword", keyword)
                .getResultList();

        List<Medicine> effectSearch = em.createQuery("select m from Medicine m " +
                "where m.effect like :keyword", Medicine.class)
                .setParameter("keyword", keyword)
                .getResultList();

        List<Medicine> saveMethodSearch = em.createQuery("select m from Medicine m " +
                "where m.saveMethod like :keyword", Medicine.class)
                .setParameter("keyword", keyword)
                .getResultList();

        List<Medicine> dosageSearch = em.createQuery("select m from Medicine m " +
                "where m.dosage like :keyword", Medicine.class)
                .setParameter("keyword", keyword)
                .getResultList();

        List<Medicine> badEffectSearch = em.createQuery("select m from Medicine m " +
                "where m.badEffect like :keyword", Medicine.class)
                .setParameter("keyword", keyword)
                .getResultList();
        List<Medicine> result = new ArrayList<>();

        for (Medicine search : badEffectSearch) {
            result.add(search);
        }

        for (Medicine search : dosageSearch) {
            result.add(search);
        }

        for (Medicine methodSearch : saveMethodSearch) {
            result.add(methodSearch);
        }

        for (Medicine search : effectSearch) {
            result.add(search);
        }

        for (Medicine search : nameSearch) {
            result.add(search);
        }

        return result;
    }
}
