package homedoctor.medicine.service;

import homedoctor.medicine.api.dto.DefaultResponse;
import homedoctor.medicine.common.ResponseMessage;
import homedoctor.medicine.common.StatusCode;
import homedoctor.medicine.domain.Alarm;
import homedoctor.medicine.domain.PrescriptionImage;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.repository.AlarmRepository;
import homedoctor.medicine.repository.PrescriptionImageRepository;
import homedoctor.medicine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrescriptionImageService {

    private final PrescriptionImageRepository prescriptionImageRepository;

    private final AlarmRepository alarmRepository;

    private final UserRepository userRepository;


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
                        .status(StatusCode.OK)
                        .message(ResponseMessage.NOT_FOUND_PRESCRIPTION)
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

    public DefaultResponse findImagesByAlarm(Long alarmId) {
        try {
            List<PrescriptionImage> images = prescriptionImageRepository.findAllByAlarm(alarmId);

            if (images.isEmpty() || images == null) {
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

    public DefaultResponse findImagesByUser(Long userId) {
        try {
            List<PrescriptionImage> images = prescriptionImageRepository.findAllByUser(userId);

            if (userId == null || images.isEmpty()) {
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
    public DefaultResponse updateImagesByAlarm(Long alarmId, List<String> images) {
        try {
            List<PrescriptionImage> imageList = prescriptionImageRepository.findAllByAlarm(alarmId);

            if (alarmId == null) {
                return DefaultResponse.response(StatusCode.METHOD_NOT_ALLOWED,
                        ResponseMessage.PRESCRIPTION_UPDATE_FAIL);
            }

            // 해당 알람 이미지 삭제
            for (PrescriptionImage prescriptionImage : imageList) {
                prescriptionImageRepository.delete(prescriptionImage.getId());
            }

            // 요청 들어온 알람 이미지 등록
            Alarm findAlarm = alarmRepository.findOne(alarmId);
            for (String image: images) {
                PrescriptionImage prescriptionImage = PrescriptionImage.builder()
                        .alarm(findAlarm)
                        .image(image)
                        .build();
                Date currentDate = new Date();


                prescriptionImage.setLastModifiedDate(currentDate);
                prescriptionImageRepository.save(prescriptionImage);
            }
            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.PRESCRIPTION_UPDATE_SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());

            return DefaultResponse.response(StatusCode.DB_ERROR,
                    ResponseMessage.DB_ERROR);
        }
    }

    @Transactional
    public DefaultResponse delete(Long prescriptionImageId) {
        try {
            PrescriptionImage image = prescriptionImageRepository.findOne(prescriptionImageId);

            if (prescriptionImageId == null || image == null) {
                return DefaultResponse.builder()
                        .status(StatusCode.METHOD_NOT_ALLOWED)
                        .message(ResponseMessage.PRESCRIPTION_DELETE_FAIL)
                        .build();
            }

            prescriptionImageRepository.delete(prescriptionImageId);

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
