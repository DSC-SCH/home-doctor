package homedoctor.medicine.repository;

import homedoctor.medicine.domain.DeactivationReason;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Repository
@RequiredArgsConstructor
public class DeactivationReasonRepository {

    @Autowired
    private EntityManager em;

    @Transactional
    public void saveReason(DeactivationReason reason) {
        em.persist(reason);
    }
}
