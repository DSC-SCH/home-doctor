package homedoctor.medicine.api.controller;

import homedoctor.medicine.api.dto.DefaultResponse;
import homedoctor.medicine.api.dto.alarm.CreateAlarmRequest;
import homedoctor.medicine.api.dto.alarm.UpdateAlarmRequest;
import homedoctor.medicine.api.dto.alarm.*;
import homedoctor.medicine.common.auth.Auth;
import homedoctor.medicine.domain.Alarm;
import homedoctor.medicine.domain.AlarmCount;
import homedoctor.medicine.domain.Label;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.service.*;
import homedoctor.medicine.common.ResponseMessage;
import homedoctor.medicine.common.StatusCode;
import homedoctor.medicine.utils.DateTimeHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AlarmApiController {

    private final AlarmService alarmService;

    private final UserService userService;

    private final JwtService jwtService;

    private final LabelService labelService;

    private final AlarmCountService alarmCountService;


    @Auth
    @PostMapping("/alarm/new")
    public DefaultResponse saveAlarm(
            @RequestHeader("Authorization") final String header,
            @RequestBody @Valid final CreateAlarmRequest request) {
        try {
            if (header == null) {
                return DefaultResponse.response(StatusCode.UNAUTHORIZED,
                        ResponseMessage.UNAUTHORIZED);
            }

            // check request content validation.
            if (!request.validProperties()) {
                return DefaultResponse.response(StatusCode.METHOD_NOT_ALLOWED,
                        ResponseMessage.NOT_CONTENT);
            }

            // userId Decoding
            Long userId = jwtService.decode(header);
            User findUser = (User) userService.findOneById(userId).getData();
            Label findLabel = (Label) labelService.findOne(request.getLabel()).getData();

            Alarm alarm = Alarm.builder()
                    .user(findUser)
                    .title(request.getTitle())
                    .label(findLabel)
                    .startDate(request.getStartDate())
                    .endDate(request.getEndDate())
                    .times(request.getTimes())
                    .repeats(request.getRepeats())
                    .alarmStatus(request.getAlarmStatus())
                    .build();
            DefaultResponse response = alarmService.save(alarm);

            // Alarm 기간 만큼 복용 횟수 데이터 생성.
            Calendar calendar = Calendar.getInstance();
            DateFormat format = new SimpleDateFormat("yyyy:MM:dd");
            calendar.setTime(request.getStartDate());

            while (calendar.getTimeInMillis() <= request.getEndDate().getTime()) {

                if (request.getRepeats().contains(calendar.get(Calendar.DAY_OF_WEEK) + "")) {
                    AlarmCount alarmCount = AlarmCount.builder()
                            .user(findUser)
                            .alarm(alarm)
                            .alarmDate(DateTimeHandler.cutTime(calendar.getTime()))
                            .count(request.getTimes().split("/").length)
                            .build();
                    alarmCountService.countSave(alarmCount);
                }

                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }


            CreateAlarmResponse createAlarmResponse = CreateAlarmResponse.builder()
                    .id(alarm.getId())
                    .build();

            return DefaultResponse.response(response.getStatus(),
                    response.getMessage(),
                    createAlarmResponse);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @GetMapping("/alarm/{alarm_id}")
    public DefaultResponse getOneAlarm(
            @RequestHeader("Authorization") final String header,
            @PathVariable("alarm_id") Long id) {
        try {
            if (header == null) {
                DefaultResponse.response(StatusCode.UNAUTHORIZED,
                        ResponseMessage.UNAUTHORIZED);
            }
            DefaultResponse response = alarmService.findAlarm(id);
            Alarm alarm = (Alarm) response.getData();

            // 빈리스트 일때
            if (alarm == null) {
                return DefaultResponse.response(StatusCode.OK,
                        ResponseMessage.NOT_FOUND_ALARM);
            }

            AlarmDto alarmDto = AlarmDto.builder()
                    .user(alarm.getUser().getId())
                    .alarmId(alarm.getId())
                    .title(alarm.getTitle())
                    .labelTitle(alarm.getLabel().getTitle())
                    .color(alarm.getLabel().getColor())
                    .label(alarm.getLabel().getId())
                    .startDate(alarm.getStartDate())
                    .endDate(alarm.getEndDate())
                    .alarmStatus(alarm.getAlarmStatus())
                    .repeats(alarm.getRepeats())
                    .times(alarm.getTimes())
                    .createdDate(alarm.getCreatedDate())
                    .lastModifiedDate(alarm.getLastModifiedDate())
                    .build();

            return DefaultResponse.response(response.getStatus(),
                    response.getMessage(),
                    alarmDto);
        } catch (Exception e) {
            log.error(e.getMessage());

            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @GetMapping("/alarm/all")
    public DefaultResponse getAlarmByUser(
            @RequestHeader("Authorization") final String header) {
        try {
            if (header == null) {
                DefaultResponse.response(StatusCode.UNAUTHORIZED,
                        ResponseMessage.UNAUTHORIZED);
            }

            Long userId = jwtService.decode(header);
            DefaultResponse userResponse = userService.findOneById(userId);
            DefaultResponse response = alarmService.findAlarmsByUser((User) userResponse.getData());
            List<Alarm> findAllAlarm = (List<Alarm>) response.getData();

            // 빈리스트 일때
            if (findAllAlarm == null) {
                String[] empty = new String[0];
                return DefaultResponse.response(StatusCode.OK,
                        ResponseMessage.NOT_FOUND_ALARM,
                        empty);
            }

            return getDefaultAlarmListResponse(response, findAllAlarm);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @GetMapping("/alarm/enable")
    public DefaultResponse getEnableAlarm(
            @RequestHeader("Authorization") final String header) {
        try {
            if (header == null) {
                DefaultResponse.response(StatusCode.UNAUTHORIZED,
                        ResponseMessage.UNAUTHORIZED);
            }
            User user = (User) userService.findOneById(jwtService.decode(header)).getData();
            DefaultResponse response = alarmService.findEnableAlarm(user);


            List<Alarm> findEnableAlarmList = (List<Alarm>) response.getData();

            // 빈리스트 일때
            if (findEnableAlarmList == null || findEnableAlarmList.isEmpty()) {
                String[] empty = new String[0];
                return DefaultResponse.response(StatusCode.OK,
                        ResponseMessage.NOT_FOUND_ALARM,
                        empty);
            }

            return getDefaultAlarmListResponse(response, findEnableAlarmList);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    private DefaultResponse getDefaultAlarmListResponse(DefaultResponse response, List<Alarm> findEnableAlarmList) {
        List<AlarmDto> alarmDtoList = findEnableAlarmList.stream()
                .map(m -> AlarmDto.builder()
                        .user(m.getUser().getId())
                        .alarmId(m.getId())
                        .title(m.getTitle())
                        .label(m.getLabel().getId())
                        .startDate(m.getStartDate())
                        .endDate(m.getEndDate())
                        .alarmStatus(m.getAlarmStatus())
                        .labelTitle(m.getLabel().getTitle())
                        .color(m.getLabel().getColor())
                        .repeats(m.getRepeats())
                        .times(m.getTimes())
                        .createdDate(m.getCreatedDate())
                        .lastModifiedDate(m.getLastModifiedDate())
                        .build())
                .collect(Collectors.toList());

        return DefaultResponse.response(response.getStatus(),
                response.getMessage(),
                alarmDtoList);
    }

    @Auth
    @PutMapping("/alarm/{alarm_id}")
    public DefaultResponse updateAlarm(
            @RequestHeader("Authorization") final String header,
            @PathVariable("alarm_id") Long alarmId,
            @RequestBody @Valid UpdateAlarmRequest request) {
        try {
            if (header == null) {
                DefaultResponse.response(StatusCode.UNAUTHORIZED,
                        ResponseMessage.UNAUTHORIZED);
            }
            // find Alarm Label
            Label findLabel = (Label) labelService.findOne(request.getLabel()).getData();

            if (!request.validProperties()) {
                return DefaultResponse.response(StatusCode.BAD_REQUEST,
                        ResponseMessage.NOT_CONTENT);
            }

            Alarm alarm = Alarm.builder()
                    .title(request.getTitle())
                    .label(findLabel)
                    .startDate(request.getStartDate())
                    .endDate(request.getEndDate())
                    .alarmStatus(request.getAlarmStatus())
                    .times(request.getTimes())
                    .repeats(request.getRepeats())
                    .build();

            Alarm findAlarm = (Alarm) alarmService.findAlarm(alarmId).getData();
            Date start = findAlarm.getStartDate();
            Date end = findAlarm.getEndDate();
            DefaultResponse response = alarmService.update(alarmId, alarm);

            // 알람의 날짜 변경시 변경된 날짜에 맞게 알람 복용 횟수 테이블도 수정.
            if (!start.equals(request.getStartDate()) ||
                    !end.equals(request.getEndDate())) {
                alarmCountService.updateCountByAlarm(findAlarm);
            }


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
    @PutMapping("/alarm/change/{alarm_id}")
    public DefaultResponse changeAlarmStatus(
            @RequestHeader("Authorization") final String header,
            @RequestBody @Valid ChangeStatusRequest changeStatus,
            @PathVariable("alarm_id") Long alarmId) {
        try {
            if (header == null) {
                DefaultResponse.response(StatusCode.UNAUTHORIZED,
                        ResponseMessage.UNAUTHORIZED);
            }

            if (changeStatus.getAlarmStatus() == null) {
                return DefaultResponse.response(StatusCode.BAD_REQUEST,
                        ResponseMessage.NOT_CONTENT);
            }

            DefaultResponse response = alarmService.changeEnableToCancel(alarmId, changeStatus.getAlarmStatus());
            return DefaultResponse.response(response.getStatus(),
                    response.getMessage());

        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @DeleteMapping("/alarm/{alarm_id}")
    public DefaultResponse deleteAlarm(
            @RequestHeader("Authorization") final String header,
            @PathVariable("alarm_id") Long id) {
        try {
            if (header == null) {
                return DefaultResponse.response(StatusCode.UNAUTHORIZED,
                        ResponseMessage.UNAUTHORIZED);
            }

            DefaultResponse response = alarmService.findAlarm(id);
            Alarm findAlarm = (Alarm) response.getData();
            // 빈리스트 일때
            if (findAlarm == null) {
                return DefaultResponse.response(response.getStatus(),
                        response.getMessage());
            }

            DefaultResponse deleteResponse = alarmService.delete(findAlarm);

            return DefaultResponse.response(deleteResponse.getStatus(),
                    deleteResponse.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
