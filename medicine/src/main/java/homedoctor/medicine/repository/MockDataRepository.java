package homedoctor.medicine.repository;

import homedoctor.medicine.domain.MockData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MockDataRepository {

    private final EntityManager em;

    public void save(MockData mockData) {
        System.out.println("====================");
        System.out.println("Mock data repo : " + mockData.getClass());
        em.persist(mockData);
    }

    public MockData findOne(Long id) {
        return em.find(MockData.class, id);
    }

    public List<MockData> findAll() {
        return em.createQuery("select m from MockData m", MockData.class)
                .getResultList();
    }

    public void deleteById(Long id) {
        em.createQuery("delete from MockData m where m.id =:id")
                .setParameter("id", id)
                .executeUpdate();
    }
}
