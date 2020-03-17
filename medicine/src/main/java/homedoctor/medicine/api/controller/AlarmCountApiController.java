package homedoctor.medicine.api.controller;


import homedoctor.medicine.api.dto.DefaultResponse;
import homedoctor.medicine.api.dto.alarmCount.*;
import homedoctor.medicine.common.ResponseMessage;
import homedoctor.medicine.common.StatusCode;
import homedoctor.medicine.common.auth.Auth;
import homedoctor.medicine.domain.AlarmCount;
import homedoctor.medicine.service.AlarmCountService;
import homedoctor.medicine.service.AlarmService;
import homedoctor.medicine.service.JwtService;
import homedoctor.medicine.utils.DateTimeHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AlarmCountApiController {

    private final AlarmCountService alarmCountService;

    private final AlarmService alarmService;

    private final JwtService jwtService;

    @Auth
    @PutMapping("/alarm/counts/{alarm_id}")
    public DefaultResponse updateAlarmCount(
            @RequestHeader("Authorization") final String header,
            @RequestBody @Valid AlarmCountChangeRequest request,
            @PathVariable("alarm_id") Long alarmId) {
        try {
            if (header == null) {
                return DefaultResponse.response(StatusCode.UNAUTHORIZED,
                        ResponseMessage.UNAUTHORIZED);
            }

            if (!request.validProperties()) {
                return DefaultResponse.response(StatusCode.BAD_REQUEST,
                        ResponseMessage.NOT_CONTENT);
            }

            DefaultResponse response = alarmCountService.updateCountByAlarmIdAndDate(alarmId,
                    DateTimeHandler.cutTime(request.getDate()),
                    request.getCount());

            return DefaultResponse.response(response.getStatus(),
                    response.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @PutMapping("/alarm/count/many")
    public DefaultResponse updateAllAlarmCounts(
            @RequestHeader("Authorization") final String header,
            @RequestBody @Valid AlarmListCountChangeRequest request) {
        try {

            if (header == null) {
                return DefaultResponse.response(StatusCode.UNAUTHORIZED,
                        ResponseMessage.UNAUTHORIZED);
            }

            if (request.getCounts() == null) {
                return DefaultResponse.response(StatusCode.BAD_REQUEST,
                        ResponseMessage.NOT_CONTENT);
            }

            DefaultResponse response = alarmCountService.updateCountByAlarmList(request.getCounts());

            return DefaultResponse.response(response.getStatus(),
                    response.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 특정 알람, 날짜에 대한 복용 횟수.
     */
    @Auth
    @GetMapping("/alarm/counts/{alarm_id}")
    public DefaultResponse getCountByAlarmDate(
            @RequestHeader("Authorization") final String header,
            @RequestBody @Valid AlarmCountRequest request,
            @PathVariable("alarm_id") Long alarmId) {
        try {
            if (request.getDate() == null) {
                return DefaultResponse.response(StatusCode.BAD_REQUEST,
                        ResponseMessage.NOT_CONTENT);
            }

            AlarmCount findAlarmCount = (AlarmCount) alarmCountService.findCountsByAlarmDate(
                    alarmId, DateTimeHandler.cutTime(request.getDate())).getData();

            AlarmCountResponse alarmCountResponse = AlarmCountResponse.builder()
                    .count(findAlarmCount.getCounts())
                    .build();

            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.ALARM_COUNT_SEARCH_SUCCESS,
                    alarmCountResponse);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 유저의 특정 날짜에 대한 모든 알람의 복용 횟수 조회
     */
    @Auth
    @PostMapping("/alarm/counts")
    public DefaultResponse getCountAllByAlarmDate(
            @RequestHeader("Authorization") final String header,
            @RequestBody CountByAlarmDateRequest request) {

        try {

            if (header == null) {
                return DefaultResponse.response(StatusCode.UNAUTHORIZED,
                        ResponseMessage.UNAUTHORIZED);
            }

            if (request.getDate() == null) {
                return DefaultResponse.response(StatusCode.BAD_REQUEST,
                        ResponseMessage.NOT_CONTENT);
            }

            Long userId = jwtService.decode(header);
            DefaultResponse userDateResponse  = alarmCountService.findAllByUserDate(
                    userId, DateTimeHandler.cutTime(request.getDate()));
            List<AlarmCount> findAlarmCountList = (List<AlarmCount>) userDateResponse.getData();

            if (findAlarmCountList == null) {
                String[] empty = new String[0];
                return DefaultResponse.response(userDateResponse.getStatus(),
                        userDateResponse.getMessage(),
                        empty);
            }

            List<CountOfAlarmDateResponse> response = findAlarmCountList.stream()
                    .map(m -> CountOfAlarmDateResponse.builder()
                            .alarmId(m.getAlarmId().getId())
                            .count(m.getCounts())
                            .build())
                    .collect(Collectors.toList());

            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.ALARM_COUNT_SEARCH_SUCCESS,
                    response);

        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));

            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 연동 유저의 특정 날짜에 대한 모든 알람의 복용 횟수 조회
     */
    @Auth
    @PostMapping("/alarm/counts/user/{careUser_id}")
    public DefaultResponse getCareUserCountAllByAlarmDate(
            @RequestHeader("Authorization") final String header,
            @RequestBody CountByAlarmDateRequest request,
            @PathVariable("careUser_id") final Long careId) {

        try {

            if (header == null) {
                return DefaultResponse.response(StatusCode.UNAUTHORIZED,
                        ResponseMessage.UNAUTHORIZED);
            }

            if (request.getDate() == null) {
                return DefaultResponse.response(StatusCode.BAD_REQUEST,
                        ResponseMessage.NOT_CONTENT);
            }


            DefaultResponse userDateResponse  = alarmCountService.findAllByUserDate(
                    careId, DateTimeHandler.cutTime(request.getDate()));
            List<AlarmCount> findAlarmCountList = (List<AlarmCount>) userDateResponse.getData();

            if (findAlarmCountList == null) {
                String[] empty = new String[0];
                return DefaultResponse.response(userDateResponse.getStatus(),
                        userDateResponse.getMessage(), empty);
            }

            List<CountOfAlarmDateResponse> response = findAlarmCountList.stream()
                    .map(m -> CountOfAlarmDateResponse.builder()
                            .alarmId(m.getAlarmId().getId())
                            .count(m.getCounts())
                            .build())
                    .collect(Collectors.toList());

            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.ALARM_COUNT_SEARCH_SUCCESS,
                    response);

        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));

            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     *
     * 특정 알람의 모든 날짜에 대한 복용 횟수 조회
     */
    @Auth
    @GetMapping("/alarm/counts/all/{alarm_id}")
    public DefaultResponse getCountAllByAlarmDate(
            @RequestHeader("Authorization") final String header,
            @PathVariable("alarm_id") final Long alarmId) {

        try {
            if (header == null) {
                return DefaultResponse.response(StatusCode.UNAUTHORIZED,
                        ResponseMessage.UNAUTHORIZED);
            }

            List<AlarmCount> alarmCountList = (List<AlarmCount>) alarmCountService.findAllByAlarm(alarmId).getData();

            if (alarmCountList == null) {
                String[] empty = new String[0];
                return DefaultResponse.response(StatusCode.BAD_REQUEST,
                        ResponseMessage.NOT_FOUND_ALARM_COUNT, empty);
            }

            List<AlarmCountResponse> alarmCountResponse = alarmCountList.stream()
                    .map(m -> AlarmCountResponse.builder()
                    .count(m.getCounts())
                    .build()).collect(Collectors.toList());

            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.ALARM_COUNT_SEARCH_SUCCESS,
                    alarmCountResponse);


        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));

            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

}
