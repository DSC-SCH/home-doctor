package homedoctor.medicine.repository;

import homedoctor.medicine.domain.Label;
import homedoctor.medicine.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LabelRepository {

    private final EntityManager em;

    public void save(Label label) {
        em.persist(label);
    }

    public Label findOne(Long id) {
        return em.find(Label.class, id);
    }

    public List<Label> findAllByUser(User user) {
        return em.createQuery("select l from Label l where l.user = :user", Label.class)
                .setParameter("user", user)
                .getResultList();
    }

    public void delete(Label label) {
        em.createQuery("select l from Label l where l.id = :id", Label.class)
                .setParameter("id", label.getId())
                .executeUpdate();
        em.clear();
    }
}
