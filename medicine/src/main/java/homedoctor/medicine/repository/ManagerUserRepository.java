package homedoctor.medicine.repository;

import homedoctor.medicine.domain.ManagerUser;
import homedoctor.medicine.domain.ReceiverUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class ManagerUserRepository {

    private final EntityManager em;

    public void save(ManagerUser managerUser, ReceiverUser receiverUser) {
        em.persist(managerUser);
        em.persist(receiverUser);
    }

    public void delete(ManagerUser managerUser) {
        em.createQuery("select m from ManagerUser m where m.id = :id", ManagerUser.class)
                .setParameter("id", managerUser.getId())
                .executeUpdate();
    }
}
