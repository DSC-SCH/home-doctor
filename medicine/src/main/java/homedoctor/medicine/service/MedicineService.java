package homedoctor.medicine.service;

import homedoctor.medicine.api.dto.DefaultResponse;
import homedoctor.medicine.common.ResponseMessage;
import homedoctor.medicine.common.StatusCode;
import homedoctor.medicine.domain.Medicine;
import homedoctor.medicine.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MedicineService {

    private final MedicineRepository medicineRepository;

    public DefaultResponse findOneMedicine(Long medicineId) {
        try {
            Medicine findMedicine = medicineRepository.findOneMedicine(medicineId);

            if (findMedicine == null) {
                return DefaultResponse.response(StatusCode.METHOD_NOT_ALLOWED,
                        ResponseMessage.NOT_FOUND_MEDICINE);
            }
            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.MEDICINE_SEARCH_SUCCESS,
                    findMedicine);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.response(StatusCode.DB_ERROR,
                    ResponseMessage.DB_ERROR);
        }
    }

    public DefaultResponse findByName(String name) {
        try {
            List<Medicine> medicines = medicineRepository.findAllByName(name);

            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.MEDICINE_SEARCH_SUCCESS,
                    medicines);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.response(StatusCode.DB_ERROR,
                    ResponseMessage.DB_ERROR);
        }
    }

    public DefaultResponse findByKeyword(String keyword) {
        try {
            List<Medicine> medicines = medicineRepository.findAllByKeyword(keyword);

            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.MEDICINE_SEARCH_SUCCESS,
                    medicines);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.response(StatusCode.DB_ERROR,
                    ResponseMessage.DB_ERROR);
        }
    }
}
