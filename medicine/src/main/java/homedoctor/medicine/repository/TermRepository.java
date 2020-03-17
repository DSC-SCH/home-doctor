package homedoctor.medicine.repository;

import homedoctor.medicine.domain.Terms;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TermRepository {

    @Autowired
    private EntityManager em;

    public List<Terms> findTerms() {
        return em.createQuery("select t from Terms t", Terms.class)
                .getResultList();
    }

    public Terms findTermsByTitle(String title) {
        return em.createQuery("select t from Terms t where t.title = :title",
                Terms.class)
                .setParameter("title", title)
                .getSingleResult();
    }

}
