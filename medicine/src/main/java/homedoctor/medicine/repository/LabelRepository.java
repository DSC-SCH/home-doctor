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
        String query = "select l from Label l join fetch l.user where l.user = :user";
        return em.createQuery(query, Label.class)
                .setParameter("user", user)
                .getResultList();
    }

    public Label findDefaultLabel(Long userId) {
        return em.createQuery("select l from Label l where l.user.id = :id and l.title = :title", Label.class)
                .setParameter("title", "없음")
                .setParameter("id", userId)
                .getSingleResult();
    }

    public void delete(Label label) {
        em.createQuery("delete from Label l where l.id = :id")
                .setParameter("id", label.getId())
                .executeUpdate();
        em.clear();
    }

}
