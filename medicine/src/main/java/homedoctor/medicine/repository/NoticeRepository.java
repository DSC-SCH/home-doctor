package homedoctor.medicine.repository;

import homedoctor.medicine.domain.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class NoticeRepository {

    @Autowired
    private EntityManager em;

    public List<Notice> findAllNotice() {
        return em.createQuery("select n from Notice n", Notice.class)
                .getResultList();
    }

    public Notice findOneNotice(Long noticeId) {
        return em.createQuery("select n from Notice n where n.noticeId = :noticeId", Notice.class)
                .setParameter("noticeId", noticeId)
                .getSingleResult();
    }
}
