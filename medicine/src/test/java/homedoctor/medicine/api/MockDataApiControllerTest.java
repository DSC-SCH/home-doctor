package homedoctor.medicine.api;

import homedoctor.medicine.domain.MockData;
import homedoctor.medicine.service.MockDataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MockDataApiControllerTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private MockDataService mockDataService;

    @Test
    public void saveMockDataTest() throws Exception {
        // given
        MockData mockData = new MockData();
        mockData.setUsername("nathan");
        mockData.setStreet("paju");
        mockData.setPhoneNumber("01066818139");

        // when
        Long id = mockDataService.save(mockData);

        // then
        assertEquals(mockDataService.findOne(id).getId(), mockData.getId());
    }
}
