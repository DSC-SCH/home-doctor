package homedoctor.medicine.service;

import homedoctor.medicine.api.dto.DefaultResponse;
import homedoctor.medicine.domain.Alarm;
import homedoctor.medicine.domain.Label;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.repository.AlarmRepository;
import homedoctor.medicine.common.ResponseMessage;
import homedoctor.medicine.common.StatusCode;
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
public class AlarmService {

    private final AlarmRepository alarmRepository;

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
    public DefaultResponse update(Long alarmId, Alarm alarm) {
        try {
            Alarm findAlarm = alarmRepository.findOne(alarmId);

            if (findAlarm == null) {
                return DefaultResponse.builder()
                        .status(StatusCode.OK)
                        .message(ResponseMessage.NOT_FOUND_ALARM)
                        .build();
            }


            findAlarm.setTitle(alarm.getTitle());
            findAlarm.setLabel(alarm.getLabel());
            findAlarm.setStartDate(alarm.getStartDate());
            findAlarm.setEndDate(alarm.getEndDate());
            findAlarm.setAlarmStatus(alarm.getAlarmStatus());
            findAlarm.setRepeats(alarm.getRepeats());
            findAlarm.setTimes(alarm.getTimes());
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
