package homedoctor.medicine.service;

import homedoctor.medicine.api.dto.DefaultResponse;
import homedoctor.medicine.domain.*;
import homedoctor.medicine.repository.AlarmRepository;
import homedoctor.medicine.common.ResponseMessage;
import homedoctor.medicine.common.StatusCode;
import homedoctor.medicine.repository.LabelRepository;
import homedoctor.medicine.repository.PrescriptionImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmService {

    @Autowired
    private final AlarmRepository alarmRepository;

    @Autowired
    private final LabelRepository labelRepository;

    @Autowired
    private final PrescriptionImageRepository prescriptionImageRepository;

    @Transactional
    public DefaultResponse save(Alarm alarm) {
        try {
            alarmRepository.save(alarm);
            return DefaultResponse.builder()
                    .status(StatusCode.OK)
                    .message(ResponseMessage.ALARM_CREATE_SUCCESS)
                    .build();
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .message(ResponseMessage.DB_ERROR)
                    .build();
        }
    }

    public DefaultResponse findAlarmsByUser(User user) {
        try {
            List<Alarm> findAllAlarmList = alarmRepository.findAllByUser(user);

            if (!findAllAlarmList.isEmpty()) {
                return DefaultResponse.builder()
                        .status(StatusCode.OK)
                        .message(ResponseMessage.ALARM_SEARCH_SUCCESS)
                        .data(findAllAlarmList)
                        .build();
            }

            return DefaultResponse.builder()
                    .status(StatusCode.METHOD_NOT_ALLOWED)
                    .message(ResponseMessage.ALARM_SEARCH_FAIL)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .message(ResponseMessage.DB_ERROR)
                    .build();
        }
    }

    public DefaultResponse findEnableAlarm(User user) {
        try {
            List<Alarm> enableAlarmList = alarmRepository.findAllByEnable(user);

            if (!enableAlarmList.isEmpty()) {
                return DefaultResponse.builder()
                        .status(StatusCode.OK)
                        .message(ResponseMessage.ALARM_SEARCH_SUCCESS)
                        .data(enableAlarmList)
                        .build();
            }

            return DefaultResponse.builder()
                                .status(StatusCode.METHOD_NOT_ALLOWED)
                                .message(ResponseMessage.ALARM_SEARCH_FAIL)
                                .build();
        } catch (Exception e) {
            log.error(e.getMessage());

            return DefaultResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .message(ResponseMessage.DB_ERROR)
                    .build();
        }
    }

    public DefaultResponse findAlarmByLabel(Long userId, Long labelId) {
        try {
            List<Alarm> findAlarm = alarmRepository.findAllByLabel(userId, labelId);

            if (findAlarm != null || !findAlarm.isEmpty()) {

                return DefaultResponse.builder()
                        .status(StatusCode.OK)
                        .message(ResponseMessage.ALARM_SEARCH_SUCCESS)
                        .data(findAlarm)
                        .build();
            }

            return DefaultResponse.builder()
                    .status(StatusCode.METHOD_NOT_ALLOWED)
                    .message(ResponseMessage.ALARM_SEARCH_FAIL)
                    .build();

        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .message(ResponseMessage.DB_ERROR)
                    .build();
        }
    }

    public DefaultResponse findAlarm(Long alarmId) {
        try {
            Alarm findAlarm = alarmRepository.findOne(alarmId);

            if (findAlarm != null) {
                return DefaultResponse.builder()
                        .status(StatusCode.OK)
                        .message(ResponseMessage.ALARM_SEARCH_SUCCESS)
                        .data(findAlarm)
                        .build();
            }

            return DefaultResponse.builder()
                    .status(StatusCode.METHOD_NOT_ALLOWED)
                    .message(ResponseMessage.ALARM_SEARCH_FAIL)
                    .data(null)
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
    public DefaultResponse changeEnableToCancel(Long alarmId, AlarmStatus alarmStatus) {
        try {
            Alarm findAlarm = alarmRepository.findOne(alarmId);

            if (findAlarm == null) {
                return DefaultResponse.builder()
                        .status(StatusCode.OK)
                        .message(ResponseMessage.NOT_FOUND_ALARM)
                        .build();
            }

            findAlarm.setAlarmStatus(alarmStatus);
            alarmRepository.save(findAlarm);

            return DefaultResponse.builder()
                    .status(StatusCode.OK)
                    .message(ResponseMessage.ALARM_UPDATE_SUCCESS)
                    .build();

        } catch (Exception e) {
            log.error(e.getMessage());
            // RollBack
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            return DefaultResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .message(ResponseMessage.DB_ERROR)
                    .build();
        }

    }


    @Transactional
    public DefaultResponse changeAlarmLabelIfDeleteLabel(Long userId, Long labelId) {
        try {
            List<Alarm> findAlarm = alarmRepository.findAllByLabel(userId, labelId);

            if (findAlarm != null || !findAlarm.isEmpty()) {

                for (Alarm alarm : findAlarm) {
                    alarmRepository.updateLabel(userId, alarm.getId());
                }

                return DefaultResponse.builder()
                        .status(StatusCode.OK)
                        .message(ResponseMessage.ALARM_CHANGE_LABEL_SUCCESS)
                        .build();
            }

            return DefaultResponse.builder()
                    .status(StatusCode.METHOD_NOT_ALLOWED)
                    .message(ResponseMessage.ALARM_SEARCH_FAIL)
                    .build();

        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return DefaultResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .message(ResponseMessage.DB_ERROR)
                    .build();
        }
    }

    @Transactional
    public DefaultResponse update(Long alarmId, Alarm alarm) {
        try {
            Alarm findAlarm = alarmRepository.findOne(alarmId);

            if (findAlarm == null) {
                return DefaultResponse.builder()
                        .status(StatusCode.OK)
                        .message(ResponseMessage.NOT_FOUND_ALARM)
                        .build();
            }

            Date currentDate = new Date();

            findAlarm.setTitle(alarm.getTitle());
            findAlarm.setLabel(alarm.getLabel());
            findAlarm.setStartDate(alarm.getStartDate());
            findAlarm.setEndDate(alarm.getEndDate());
            findAlarm.setAlarmStatus(alarm.getAlarmStatus());
            findAlarm.setRepeats(alarm.getRepeats());
            findAlarm.setTimes(alarm.getTimes());
            findAlarm.setLastModifiedDate(currentDate);

            alarmRepository.save(findAlarm);

            return DefaultResponse.builder()
                    .status(StatusCode.OK)
                    .message(ResponseMessage.ALARM_UPDATE_SUCCESS)
                    .build();

        } catch (Exception e) {
            log.error(e.getMessage());
            // RollBack
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            return DefaultResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .message(ResponseMessage.DB_ERROR)
                    .build();
        }
    }

    @Transactional
    public DefaultResponse delete(Alarm alarm) {
        try {
            Alarm findAlarm = alarmRepository.findOne(alarm.getId());
            if (findAlarm == null) {
                return DefaultResponse.builder()
                        .status(StatusCode.METHOD_NOT_ALLOWED)
                        .message(ResponseMessage.NOT_FOUND_ALARM)
                        .build();
            }

            List<PrescriptionImage> prescriptionImageList = prescriptionImageRepository.findAllByAlarm(alarm.getId());

            if (prescriptionImageList == null) {
                return DefaultResponse.response(StatusCode.METHOD_NOT_ALLOWED,
                        ResponseMessage.PRESCRIPTION_SEARCH_FAIL);
            }

            // 기존에 등록된 알람에 이미지 삭제.
            if (!prescriptionImageList.isEmpty()) {
                for (PrescriptionImage prescriptionImage : prescriptionImageList) {
                    prescriptionImageRepository.delete(prescriptionImage.getId());
                }
            }


            alarmRepository.delete(findAlarm);
            return DefaultResponse.builder()
                    .status(StatusCode.OK)
                    .message(ResponseMessage.ALARM_DELETE_SUCCESS)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            // RollBack
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DefaultResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .message(ResponseMessage.DB_ERROR)
                    .build();
        }
    }
}
