package homedoctor.medicine.service;

import homedoctor.medicine.api.dto.alarm.request.CreateAlarmRequest;
import homedoctor.medicine.api.dto.alarm.request.UpdateAlarmRequest;
import homedoctor.medicine.domain.Alarm;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.dto.DefaultAlarmResponse;
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
    public DefaultAlarmResponse save(CreateAlarmRequest request) {
        try {
            if (request.validProperties()) {
                Alarm alarm = Alarm.builder()
                        .user(request.getUser())
                        .title(request.getTitle())
                        .label(request.getLabel())
                        .startDate(request.getStartDate())
                        .endDate(request.getEndDate())
                        .times(request.getTimes())
                        .repeats(request.getRepeats())
                        .alarmStatus(request.getAlarmStatus())
                        .build();
                alarmRepository.save(alarm);
                return DefaultAlarmResponse.builder()
                        .status(StatusCode.OK)
                        .responseMessage(ResponseMessage.ALARM_CREATE_SUCCESS)
                        .alarm(alarm)
                        .alarmList(null)
                        .build();
            }

            return DefaultAlarmResponse.builder()
                        .status(StatusCode.METHOD_NOT_ALLOWED)
                        .responseMessage(ResponseMessage.NOT_CONTENT)
                        .alarm(null)
                        .alarmList(null)
                        .build();
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultAlarmResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .responseMessage(ResponseMessage.DB_ERROR)
                    .alarm(null)
                    .alarmList(null)
                    .build();
        }
    }

    public DefaultAlarmResponse findAlarmsByUser(User user) {
        try {
            List<Alarm> findAllAlarmList = alarmRepository.findAllByUser(user);

            if (!findAllAlarmList.isEmpty()) {
                return DefaultAlarmResponse.builder()
                        .status(StatusCode.OK)
                        .responseMessage(ResponseMessage.ALARM_SEARCH_SUCCESS)
                        .alarmList(findAllAlarmList)
                        .alarm(null)
                        .build();
            }

            return DefaultAlarmResponse.builder()
                    .status(StatusCode.METHOD_NOT_ALLOWED)
                    .responseMessage(ResponseMessage.ALARM_SEARCH_FAIL)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultAlarmResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .responseMessage(ResponseMessage.DB_ERROR)
                    .alarm(null)
                    .alarmList(null)
                    .build();
        }
    }

    public DefaultAlarmResponse findEnableAlarm(User user) {
        try {
            List<Alarm> enableAlarmList = alarmRepository.findAllByEnable(user);
            if (!enableAlarmList.isEmpty()) {
                return DefaultAlarmResponse.builder()
                        .status(StatusCode.OK)
                        .responseMessage(ResponseMessage.ALARM_SEARCH_SUCCESS)
                        .alarm(null)
                        .alarmList(enableAlarmList)
                        .build();
            }

            return DefaultAlarmResponse.builder()
                                .status(StatusCode.OK)
                                .responseMessage(ResponseMessage.NOT_FOUND_ALARM)
                                .alarm(null)
                                .alarmList(null)
                                .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultAlarmResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .responseMessage(ResponseMessage.DB_ERROR)
                    .alarm(null)
                    .alarmList(null)
                    .build();
        }
    }

    public DefaultAlarmResponse findAlarm(Long alarmId) {
        try {
            Alarm findAlarm = alarmRepository.findOne(alarmId);

            if (findAlarm != null) {
                return DefaultAlarmResponse.builder()
                        .status(StatusCode.OK)
                        .responseMessage(ResponseMessage.ALARM_SEARCH_SUCCESS)
                        .alarm(findAlarm)
                        .alarmList(null)
                        .build();
            }

            return DefaultAlarmResponse.builder()
                    .status(StatusCode.OK)
                    .responseMessage(ResponseMessage.ALARM_SEARCH_FAIL)
                    .alarm(null)
                    .alarmList(null)
                    .build();

        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultAlarmResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .responseMessage(ResponseMessage.DB_ERROR)
                    .alarm(null)
                    .alarmList(null)
                    .build();
        }
    }

    @Transactional
    public DefaultAlarmResponse update(Long alarmId, UpdateAlarmRequest updateAlarmRequest) {
        try {
            Alarm findAlarm = alarmRepository.findOne(alarmId);

            if (findAlarm == null) {
                return DefaultAlarmResponse.builder()
                        .status(StatusCode.OK)
                        .responseMessage(ResponseMessage.NOT_FOUND_ALARM)
                        .alarm(null)
                        .alarmList(null)
                        .build();
            }

            if (updateAlarmRequest.validProperties() == true) {
                findAlarm.setTitle(updateAlarmRequest.getTitle());
                findAlarm.setLabel(updateAlarmRequest.getLabel());
                findAlarm.setStartDate(updateAlarmRequest.getStartDate());
                findAlarm.setEndDate(updateAlarmRequest.getEndDate());
                findAlarm.setAlarmStatus(updateAlarmRequest.getAlarmStatus());
                findAlarm.setRepeats(updateAlarmRequest.getRepeats());
                findAlarm.setTimes(updateAlarmRequest.getTimes());
                alarmRepository.save(findAlarm);

                return DefaultAlarmResponse.builder()
                        .status(StatusCode.OK)
                        .responseMessage(ResponseMessage.ALARM_UPDATE_SUCCESS)
                        .alarm(findAlarm)
                        .alarmList(null)
                        .build();
            }

            return DefaultAlarmResponse.builder()
                    .status(StatusCode.METHOD_NOT_ALLOWED)
                    .responseMessage(ResponseMessage.NOT_CONTENT)
                    .alarm(null)
                    .alarmList(null)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            // RollBack
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            return DefaultAlarmResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .responseMessage(ResponseMessage.DB_ERROR)
                    .alarm(null)
                    .alarmList(null)
                    .build();
        }
    }

    @Transactional
    public DefaultAlarmResponse delete(Alarm alarm) {
        try {
            Alarm findAlarm = alarmRepository.findOne(alarm.getId());
            if (findAlarm == null) {
                return DefaultAlarmResponse.builder()
                        .status(StatusCode.METHOD_NOT_ALLOWED)
                        .responseMessage(ResponseMessage.NOT_FOUND_ALARM)
                        .alarm(null)
                        .alarmList(null)
                        .build();
            }
            return DefaultAlarmResponse.builder()
                    .status(StatusCode.OK)
                    .responseMessage(ResponseMessage.ALARM_DELETE_SUCCESS)
                    .alarm(null)
                    .alarmList(null)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            // RollBack
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DefaultAlarmResponse.builder()
                    .status(StatusCode.DB_ERROR)
                    .responseMessage(ResponseMessage.DB_ERROR)
                    .alarm(null)
                    .alarmList(null)
                    .build();
        }
    }
}
