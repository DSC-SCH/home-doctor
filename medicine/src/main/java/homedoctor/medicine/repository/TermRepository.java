package homedoctor.medicine.repository;

import homedoctor.medicine.domain.Terms;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TermRepository {

    private EntityManager em;

    public List<Terms> findTerms() {
        return em.createQuery("select t from Terms t", Terms.class)
                .getResultList();
    }

}
