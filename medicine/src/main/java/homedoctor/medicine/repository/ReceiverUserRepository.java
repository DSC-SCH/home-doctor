package homedoctor.medicine.repository;

import homedoctor.medicine.domain.ReceiverUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class ReceiverUserRepository {

    private final EntityManager em;

    public void save(ReceiverUser receiverUser) {
        em.persist(receiverUser);
    }

    public void delete(ReceiverUser receiverUser) {
        em.createQuery(
                "select r from ReceiverUser r where r.id = :id", ReceiverUser.class)
                .setParameter("id", receiverUser.getId())
                .executeUpdate();
    }
}
