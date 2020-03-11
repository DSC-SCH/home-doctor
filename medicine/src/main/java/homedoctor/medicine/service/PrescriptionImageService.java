package homedoctor.medicine.service;

import homedoctor.medicine.api.dto.DefaultResponse;
import homedoctor.medicine.common.ResponseMessage;
import homedoctor.medicine.common.StatusCode;
import homedoctor.medicine.domain.Alarm;
import homedoctor.medicine.domain.PrescriptionImage;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.repository.PrescriptionImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrescriptionImageService {

    private final PrescriptionImageRepository prescriptionImageRepository;
    private final AlarmService alarmService;
    private final UserService userService;


    @Transactional
    public DefaultResponse save(PrescriptionImage image) {
        try {
            prescriptionImageRepository.save(image);

            return DefaultResponse.builder()
                    .status(StatusCode.OK)
                    .message(ResponseMessage.PRESCRIPTION_CREATE_SUCCESS)
                    .build();

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .message(ResponseMessage.DB_ERROR)
                    .build();
        }
    }

    public DefaultResponse findOneImage(Long id) {
        try {
            PrescriptionImage findImage = prescriptionImageRepository.findOne(id);

            if (findImage == null) {
                return DefaultResponse.builder()
                        .status(StatusCode.METHOD_NOT_ALLOWED)
                        .message(ResponseMessage.PRESCRIPTION_SEARCH_FAIL)
                        .build();
            }
            return DefaultResponse.builder()
                    .status(StatusCode.OK)
                    .message(ResponseMessage.PRESCRIPTION_SEARCH_SUCCESS)
                    .data(findImage)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.builder()
                    .status(StatusCode.METHOD_NOT_ALLOWED)
                    .message(ResponseMessage.NOT_FOUND_PRESCRIPTION)
                    .build();
        }
    }

    public DefaultResponse findImagesByAlarm(Alarm alarm) {
        try {
            DefaultResponse alarmResponse = alarmService.findAlarm(alarm.getId());
            Alarm findAlarm = (Alarm) alarmResponse.getData();
            List<PrescriptionImage> images = prescriptionImageRepository.findAllByAlarm(alarm);

            if (findAlarm == null || images.isEmpty()) {
                return DefaultResponse.builder()
                        .status(StatusCode.METHOD_NOT_ALLOWED)
                        .message(ResponseMessage.PRESCRIPTION_SEARCH_FAIL)
                        .build();
            }

            return DefaultResponse.builder()
                    .status(StatusCode.OK)
                    .message(ResponseMessage.PRESCRIPTION_SEARCH_SUCCESS)
                    .data(images)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .message(ResponseMessage.DB_ERROR)
                    .build();
        }
    }

    public DefaultResponse findImagesByUser(User user) {
        try {
            List<PrescriptionImage> images = prescriptionImageRepository.findAllByUser(user);

            if (user == null || images.isEmpty()) {
                return DefaultResponse.builder()
                        .status(StatusCode.METHOD_NOT_ALLOWED)
                        .message(ResponseMessage.PRESCRIPTION_SEARCH_FAIL)
                        .build();
            }

            return DefaultResponse.builder()
                    .status(StatusCode.OK)
                    .message(ResponseMessage.PRESCRIPTION_SEARCH_SUCCESS)
                    .data(images)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .message(ResponseMessage.DB_ERROR)
                    .build();
        }
    }

    @Transactional
    public DefaultResponse delete(PrescriptionImage prescriptionImage) {
        try {
            PrescriptionImage image = prescriptionImageRepository.findOne(prescriptionImage.getId());

            if (prescriptionImage == null || image == null) {
                return DefaultResponse.builder()
                        .status(StatusCode.METHOD_NOT_ALLOWED)
                        .message(ResponseMessage.PRESCRIPTION_DELETE_FAIL)
                        .build();
            }

            prescriptionImageRepository.delete(prescriptionImage);

            return DefaultResponse.builder()
                    .status(StatusCode.OK)
                    .message(ResponseMessage.PRESCRIPTION_DELETE_SUCCESS)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DefaultResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .message(ResponseMessage.DB_ERROR)
                    .build();
        }
    }
}
