package homedoctor.medicine.service;

import homedoctor.medicine.domain.MockData;
import homedoctor.medicine.repository.MockDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MockDataService {

    private final MockDataRepository mockDataRepository;

    @Transactional
    public Long save(MockData mockData) {
        mockDataRepository.save(mockData);
        return mockData.getId();
    }

    public List<MockData> findAllMockData() {
        return mockDataRepository.findAll();
    }

    @Transactional
    public void update(Long id, String username, String street, String phoneNumber) {
        MockData mockData = mockDataRepository.findOne(id);
        mockData.setUsername(username);
        mockData.setStreet(street);
        mockData.setPhoneNumber(phoneNumber);
    }

    public MockData findOne(Long id) {
        return mockDataRepository.findOne(id);
    }

    @Transactional
    public void delete(Long id) {
        mockDataRepository.deleteById(id);
    }
}
