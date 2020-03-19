package homedoctor.medicine.repository;

import homedoctor.medicine.domain.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Repository
@RequiredArgsConstructor
public class QuestionRepository {

    @Autowired
    private EntityManager em;


    @Transactional
    public void save(Question question) {
        em.persist(question);
    }
}
