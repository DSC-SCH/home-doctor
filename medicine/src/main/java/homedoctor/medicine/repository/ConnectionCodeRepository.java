package homedoctor.medicine.repository;

import homedoctor.medicine.domain.ConnectionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.util.List;

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

    public ConnectionCode findOneByUser(Long userId) {
        Long tableCounts = em.createQuery("select count(c) from ConnectionCode c", Long.class).getSingleResult();

        if (tableCounts == 0) {
            return null;
        }

        List<ConnectionCode> connectionCode = em.createQuery("select c from ConnectionCode c " +
                "where c.user.id = :id", ConnectionCode.class)
                .setParameter("id", userId)
                .getResultList();

        if (connectionCode == null || connectionCode.isEmpty()) {
            return null;
        }


        return connectionCode.get(0);
    }

    // 같은 숫자 코드가 있는지 확인
    public boolean isSameCode(String code) {
        Long tableCounts = em.createQuery("select count(c) from ConnectionCode c", Long.class).getSingleResult();

        if (tableCounts == 0) {
            return false;
        }

        List<ConnectionCode> connectionCodes = em.createQuery("select c from ConnectionCode c " +
                        "where c.code = :code",
                ConnectionCode.class)
                .setParameter("code", code)
                .getResultList();

        if (connectionCodes == null || connectionCodes.isEmpty()) {
            return false;
        }

        return true;
    }

    public ConnectionCode getCode(String code) {
        return em.createQuery("select c from ConnectionCode c " +
                        "where c.code = :code",
                ConnectionCode.class)
                .setParameter("code", code)
                .getSingleResult();
    }

    public void delete(Long id) {
        em.createQuery("delete from ConnectionCode c where c.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }
}
