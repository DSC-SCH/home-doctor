package homedoctor.medicine.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import homedoctor.medicine.api.dto.DefaultResponse;
import homedoctor.medicine.common.ResponseMessage;
import homedoctor.medicine.common.StatusCode;
import homedoctor.medicine.domain.Alarm;
import homedoctor.medicine.domain.AlarmCount;
import homedoctor.medicine.repository.AlarmCountRepository;
import homedoctor.medicine.repository.AlarmRepository;
import homedoctor.medicine.utils.DateTimeHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmCountService {

    private final AlarmRepository alarmRepository;

    private final AlarmCountRepository alarmCountRepository;

    @Transactional
    public DefaultResponse saveByAlarm(List<AlarmCount> alarmCountList) {
        try {

            if (alarmCountList == null || alarmCountList.isEmpty()) {
                return DefaultResponse.response(StatusCode.BAD_REQUEST,
                        ResponseMessage.ALARM_COUNT_CREATE_FAIL);
            }

            for (AlarmCount alarmCount : alarmCountList) {
                alarmCountRepository.save(alarmCount);
            }

            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.ALARM_COUNT_CREATE_SUCCESS);
        } catch (Exception e) {
            return DefaultResponse.response(StatusCode.DB_ERROR,
                    ResponseMessage.DB_ERROR);
        }
    }


    @Transactional
    public DefaultResponse countSave(AlarmCount alarmCount) {
        try {
            alarmCountRepository.save(alarmCount);
            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.ALARM_COUNT_CREATE_SUCCESS);

        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));

            return DefaultResponse.response(StatusCode.DB_ERROR,
                    ResponseMessage.DB_ERROR);
        }
    }

    public DefaultResponse findCountsByAlarmDate(Long alarmId, String date) {
        try {
            AlarmCount alarmCount = alarmCountRepository.findOneByAlarmDate(alarmId, date);

            if (alarmCount == null) {
                return DefaultResponse.response(StatusCode.BAD_REQUEST,
                        ResponseMessage.ALARM_COUNT_SEARCH_FAIL);
            }

            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.ALARM_COUNT_SEARCH_SUCCESS,
                    alarmCount);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));

            return DefaultResponse.response(StatusCode.DB_ERROR,
                    ResponseMessage.DB_ERROR);
        }
    }


    /**
     * 유저의 특정 날짜의 모든 복용횟수 가져오는 서비스
     *
     * @param userId : 사용자 고유값
     * @param date : 사용자의 알람 날짜.
     * @return
     */
    public DefaultResponse findAllByUserDate(Long userId, String date) {
        try {
            List<AlarmCount> alarmCountList = alarmCountRepository.findAllByUserDate(userId, date);

            if (alarmCountList == null || alarmCountList.isEmpty()) {
                return DefaultResponse.response(StatusCode.BAD_REQUEST,
                        ResponseMessage.NOT_FOUND_ALARM_COUNT);
            }

            return DefaultResponse.response(StatusCode.OK, ResponseMessage.ALARM_COUNT_SEARCH_SUCCESS,
                    alarmCountList);
        } catch (Exception e) {
            return DefaultResponse.response(StatusCode.DB_ERROR,
                    ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 특정 알람의 모든 날짜에 대해 복용 횟수 가져오는 서비스.
     * @param alarmId
     * @return
     */
    public DefaultResponse findAllByAlarm(Long alarmId) {
        try {
            List<AlarmCount> alarmCountList = alarmCountRepository.findAllByAlarm(alarmId);

            if (alarmCountList == null || alarmCountList.isEmpty()) {
                return DefaultResponse.response(StatusCode.BAD_REQUEST,
                        ResponseMessage.NOT_FOUND_ALARM_COUNT);
            }
            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.ALARM_COUNT_SEARCH_SUCCESS,
                    alarmCountList);
        } catch (Exception e) {
            return DefaultResponse.response(StatusCode.DB_ERROR,
                    ResponseMessage.DB_ERROR);
        }
    }

    @Transactional
    public DefaultResponse updateCountByAlarmIdAndDate(Long alarmId, String date, Integer count) {
        try {
            AlarmCount alarmCount = alarmCountRepository.findOneByAlarmDate(alarmId, date);

            if (alarmCount == null) {
                return DefaultResponse.response(StatusCode.BAD_REQUEST,
                        ResponseMessage.ALARM_COUNT_SEARCH_FAIL);
            }

            alarmCount.updateCounts(count);
            alarmCountRepository.save(alarmCount);

            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.ALARM_COUNT_UPDATE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return DefaultResponse.response(StatusCode.DB_ERROR,
                    ResponseMessage.DB_ERROR);
        }
    }

    @Transactional
    public DefaultResponse updateCountByAlarmList(JsonArray alarmJsonArray) {
        try {
            for (int i = 0; i < alarmJsonArray.size(); i++) {
                JsonObject tmp = (JsonObject) alarmJsonArray.get(i);

                // Json 데이터 가져오기
                Long alarmId = tmp.get("alarmId").getAsLong();
                Integer count = tmp.get("count").getAsInt();
                String date = tmp.get("date").getAsString();

                // alarmId, date 값으로 변경할 복용 횟수 테이블 고유값 가져오기
                AlarmCount findAlarmCount = alarmCountRepository.findOneByAlarmDate(alarmId, date);
                findAlarmCount.updateCounts(count);
                alarmCountRepository.save(findAlarmCount);
            }

            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.ALARM_COUNT_UPDATE_SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
            return DefaultResponse.response(StatusCode.DB_ERROR,
                    ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 알람 수정시 날짜가 변경되면 날짜에 맞춰 복용횟수 수정해주는 서비스
     * @param alarm : 수정되는 알
     * @return
     */
    @Transactional
    public DefaultResponse updateCountByAlarm(Alarm alarm) {
        try {

            // 기존 알람의 복용 횟수 삭제
            alarmCountRepository.deleteAllAlarmCountByAlarm(alarm.getId());

            // Alarm 기간 만큼 복용 횟수 데이터 생성.
            Calendar calendar = Calendar.getInstance();
            DateFormat format = new SimpleDateFormat("yyyy:MM:dd");
            calendar.setTime(alarm.getStartDate());

            while (calendar.getTimeInMillis() <= alarm.getEndDate().getTime()) {

                if (alarm.getRepeats().contains(calendar.get(Calendar.DAY_OF_WEEK) + "")) {
                    AlarmCount alarmCount = AlarmCount.builder()
                            .user(alarm.getUser())
                            .alarm(alarm)
                            .alarmDate(DateTimeHandler.cutTime(calendar.getTime()))
                            .count(alarm.getTimes().split("/").length)
                            .build();
                    alarmCountRepository.save(alarmCount);
                }

                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.ALARM_COUNT_UPDATE_SUCCESS);

        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));

            return DefaultResponse.response(StatusCode.DB_ERROR,
                    ResponseMessage.DB_ERROR);
        }

    }

    // 알람 기간 수정시 해당 알람 카운트 테이블 날짜변경 -> 삭제 후 생성.
//    @Transactional
//    public DefaultResponse changeAlarmCountDate(Long alarmCountId, Date startDate, Date endDate) {
//        try {
//            Date
//        }
//    }
}
