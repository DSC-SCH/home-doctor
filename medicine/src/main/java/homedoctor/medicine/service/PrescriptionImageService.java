package homedoctor.medicine.service;

import homedoctor.medicine.api.dto.image.CreateImageRequest;
import homedoctor.medicine.common.ResponseMessage;
import homedoctor.medicine.common.StatusCode;
import homedoctor.medicine.domain.Alarm;
import homedoctor.medicine.domain.PrescriptionImage;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.dto.DefaultAlarmResponse;
import homedoctor.medicine.dto.DefaultImageResponse;
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
    public DefaultImageResponse save(CreateImageRequest request) {
        try {

            if (request.validProperties()) {
                PrescriptionImage image = PrescriptionImage.builder()
                        .image(request.getImage())
                        .alarm(request.getAlarm())
                        .build();
                prescriptionImageRepository.save(image);

                return DefaultImageResponse.builder()
                        .status(StatusCode.OK)
                        .responseMessage(ResponseMessage.PRESCRIPTION_CREATE_SUCCESS)
                        .image(image)
                        .build();
            }

            return DefaultImageResponse.builder()
                    .status(StatusCode.METHOD_NOT_ALLOWED)
                    .responseMessage(ResponseMessage.PRESCRIPTION_CREATE_FAIL)
                    .build();
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultImageResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .responseMessage(ResponseMessage.DB_ERROR)
                    .build();
        }
    }

    public DefaultImageResponse findOneImage(Long id) {
        try {
            PrescriptionImage response = prescriptionImageRepository.findOne(id);

            if (response == null) {
                return DefaultImageResponse.builder()
                        .status(StatusCode.METHOD_NOT_ALLOWED)
                        .responseMessage(ResponseMessage.PRESCRIPTION_SEARCH_FAIL)
                        .build();
            }
            return DefaultImageResponse.builder()
                    .status(StatusCode.OK)
                    .responseMessage(ResponseMessage.PRESCRIPTION_SEARCH_SUCCESS)
                    .image(response)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultImageResponse.builder()
                    .status(StatusCode.METHOD_NOT_ALLOWED)
                    .responseMessage(ResponseMessage.NOT_FOUND_PRESCRIPTION)
                    .build();
        }
    }

    public DefaultImageResponse findImagesByAlarm(Alarm alarm) {
        try {
            DefaultAlarmResponse alarmResponse = alarmService.findAlarm(alarm.getId());
            Alarm findAlarm = alarmResponse.getAlarm();
            List<PrescriptionImage> images = prescriptionImageRepository.findAllByAlarm(alarm);

            if (findAlarm == null || images.isEmpty()) {
                return DefaultImageResponse.builder()
                        .status(StatusCode.METHOD_NOT_ALLOWED)
                        .responseMessage(ResponseMessage.NOT_FOUND_PRESCRIPTION)
                        .build();
            }

            return DefaultImageResponse.builder()
                    .status(StatusCode.OK)
                    .responseMessage(ResponseMessage.PRESCRIPTION_SEARCH_SUCCESS)
                    .imageList(images)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultImageResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .responseMessage(ResponseMessage.DB_ERROR)
                    .build();
        }
    }

    public DefaultImageResponse findImagesByUser(User user) {
        try {
            List<PrescriptionImage> images = prescriptionImageRepository.findAllByUser(user);

            if (user == null || images.isEmpty()) {
                return DefaultImageResponse.builder()
                        .status(StatusCode.METHOD_NOT_ALLOWED)
                        .responseMessage(ResponseMessage.PRESCRIPTION_SEARCH_FAIL)
                        .build();
            }

            return DefaultImageResponse.builder()
                    .status(StatusCode.OK)
                    .responseMessage(ResponseMessage.PRESCRIPTION_SEARCH_SUCCESS)
                    .imageList(images)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultImageResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .responseMessage(ResponseMessage.DB_ERROR)
                    .build();
        }
    }

    @Transactional
    public DefaultImageResponse delete(PrescriptionImage prescriptionImage) {
        try {
            PrescriptionImage image = prescriptionImageRepository.findOne(prescriptionImage.getId());

            if (prescriptionImage == null || image == null) {
                return DefaultImageResponse.builder()
                        .status(StatusCode.METHOD_NOT_ALLOWED)
                        .responseMessage(ResponseMessage.PRESCRIPTION_DELETE_FAIL)
                        .build();
            }

            prescriptionImageRepository.delete(prescriptionImage);

            return DefaultImageResponse.builder()
                    .status(StatusCode.OK)
                    .responseMessage(ResponseMessage.PRESCRIPTION_DELETE_SUCCESS)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DefaultImageResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .responseMessage(ResponseMessage.DB_ERROR)
                    .build();
        }
    }
}
