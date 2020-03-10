package homedoctor.medicine.repository;

import homedoctor.medicine.domain.ConnectionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class ConnectionCodeRepository {

    private final EntityManager em;

    public void save(ConnectionCode code) {
        em.persist(code);
    }

    public ConnectionCode findOne(Long id) {
        return em.find(ConnectionCode.class, id);
    }
}
