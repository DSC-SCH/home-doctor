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
        return em.createQuery("select m from Medicine m where m.name like :name", Medicine.class)
                .setParameter("name", "%" + name + "%")
                .getResultList();
    }

    public List<Medicine> findAllByKeyword(String keyword) {


        List<Medicine> nameSearch = em.createQuery("select m from Medicine m " +
                "where m.name like :keyword", Medicine.class)
                .setParameter("keyword", "%" + keyword + "%")
                .getResultList();

        List<Medicine> effectSearch = em.createQuery("select m from Medicine m " +
                "where m.effect like :keyword", Medicine.class)
                .setParameter("keyword", "%" + keyword + "%")
                .getResultList();

        List<Medicine> precautions = em.createQuery("select m from Medicine m " +
                "where m.precaution like :keyword", Medicine.class)
                .setParameter("keyword", "%" + keyword + "%")
                .getResultList();
        List<Medicine> result = new ArrayList<>();

        for (Medicine medicine : nameSearch) {
            result.add(medicine);
        }

        for (Medicine search : precautions) {
            result.add(search);
        }

        for (Medicine search : effectSearch) {
            result.add(search);
        }

        return result;
    }
}
