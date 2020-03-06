package homedoctor.medicine.repository;

import homedoctor.medicine.domain.ConnectCode;
import homedoctor.medicine.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class ConnectRepository {

    private final EntityManager em;

    public void save(ConnectCode code) {
        em.persist(code);
    }

    public ConnectCode findOne(Long id) {
        return em.find(ConnectCode.class, id);
    }

}
