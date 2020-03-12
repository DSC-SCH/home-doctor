package homedoctor.medicine.api.controller;

import homedoctor.medicine.api.dto.DefaultResponse;
import homedoctor.medicine.api.dto.medicine.MedicineDto;
import homedoctor.medicine.common.ResponseMessage;
import homedoctor.medicine.common.StatusCode;
import homedoctor.medicine.domain.Medicine;
import homedoctor.medicine.service.MedicineService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@AllArgsConstructor
public class MedicineApiController {

    private final MedicineService medicineService;

    @GetMapping("/search/name/{name}")
    public DefaultResponse searchMedicineByName(
            @PathVariable("name") String name) {
        try {
            List<Medicine> medicineList = (List<Medicine>) medicineService.findByName(name).getData();

            if (medicineList == null) {
                String[] empty = new String[0];
                return DefaultResponse.response(StatusCode.OK,
                        ResponseMessage.NOT_FOUND_MEDICINE,
                        empty);
            }

            List<MedicineDto> medicineDtoList = medicineList.stream()
                    .map(m -> MedicineDto.builder()
                            .medicineId(m.getId())
                            .name(m.getName())
                            .effect(m.getEffect())
                            .build())
                    .collect(Collectors.toList());

            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.MEDICINE_SEARCH_SUCCESS,
                    medicineDtoList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search/keyword/{keyword}")
    public DefaultResponse searchMedicineByKeyword(
            @PathVariable("keyword") String keyword) {
        try {
            List<Medicine> medicineList = (List<Medicine>) medicineService.findByKeyword(keyword).getData();


            if (medicineList == null) {
                String[] empty = new String[0];
                return DefaultResponse.response(StatusCode.OK,
                        ResponseMessage.NOT_FOUND_MEDICINE,
                        empty);
            }
            List<MedicineDto> medicineDtoList = medicineList.stream()
                    .map(m -> MedicineDto.builder()
                            .medicineId(m.getId())
                            .name(m.getName())
                            .effect(m.getEffect())
                            .build())
                    .collect(Collectors.toList());

            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.MEDICINE_SEARCH_SUCCESS,
                    medicineDtoList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search/{medicine_id}")
    public DefaultResponse getMedicineDetail(
            @PathVariable("medicine_id") Long id) {
        try {
            Medicine medicine = (Medicine) medicineService.findOneMedicine(id).getData();

            if (medicine == null) {
                String[] empty = new String[0];
                return DefaultResponse.response(StatusCode.OK,
                        ResponseMessage.NOT_FOUND_MEDICINE);
            }
            MedicineDto medicineDto = MedicineDto.builder()
                    .name(medicine.getName())
                    .effect(medicine.getEffect())
                    .dosage(medicine.getDosage())
                    .badEffect(medicine.getBadEffect())
                    .saveMethod(medicine.getSaveMethod())
                    .validDate(medicine.getValidDate())
                    .build();

            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.MEDICINE_SEARCH_SUCCESS,
                    medicineDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
