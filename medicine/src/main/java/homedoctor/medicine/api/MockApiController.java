package homedoctor.medicine.api;

import homedoctor.medicine.domain.MockData;
import homedoctor.medicine.dto.user.CreateMockDataRequest;
import homedoctor.medicine.dto.user.CreateMockDataResponse;
import homedoctor.medicine.dto.user.UpdateMockDataRequest;
import homedoctor.medicine.dto.user.UpdateMockDataResponse;
import homedoctor.medicine.service.MockDataService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MockApiController {

    private MockDataService mockDataService;

    @GetMapping("/api/mock/{mock_id}")
    public MockData getMockDataOne(
            @PathVariable("mock_id") Long id) {
        return mockDataService.findOne(id);
    }

    @GetMapping("/api/mock")
    public Result mockAllData() {
        List<MockData> findMockAllData = mockDataService.findAllMockData();
        List<MockDataDto> collect = findMockAllData.stream()
                .map(m -> new MockDataDto(m.getUsername(), m.getStreet(), m.getPhoneNumber()))
                .collect(Collectors.toList());

        return new Result(collect.size(), collect);
    }

    @PostMapping(value = "/api/mock")
    public CreateMockDataResponse saveMockData(@RequestBody @Valid
                                                       CreateMockDataRequest request) {
        MockData mockData = new MockData();
        mockData.setUsername(request.getUsername());
        mockData.setStreet(request.getStreet());
        mockData.setPhoneNumber(request.getPhoneNumber());
        Long id = mockDataService.save(mockData);
        return new CreateMockDataResponse(mockData.getId(), mockData.getUsername(),
                mockData.getStreet(), mockData.getPhoneNumber());
    }


    @PutMapping("/api/mock/{mock_id}")
    public UpdateMockDataResponse updateMockData(
            @PathVariable("mock_id") Long id,
            @RequestBody @Valid UpdateMockDataRequest request) {
        mockDataService.update(id, request.getUsername(), request.getStreet(), request.getPhoneNumber());
        MockData findMockData = mockDataService.findOne(id);

        return new UpdateMockDataResponse(findMockData.getId(), findMockData.getUsername(),
                findMockData.getStreet(), findMockData.getPhoneNumber());
    }

    @DeleteMapping("/api/mock/{mock_id}")
    public void deleteMockDataResponse (
            @PathVariable("mock_id") Long id) {
        mockDataService.delete(id);
    }


    @Data
    @AllArgsConstructor
    private static class Result<T> {
        private int counts;
        private T data;
    }

    @Data
    @AllArgsConstructor
    private static class MockDataDto {
        private String username;
        private String street;
        private String phoneNumber;
    }
}
