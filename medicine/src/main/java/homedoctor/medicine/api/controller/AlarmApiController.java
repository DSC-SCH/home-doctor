package homedoctor.medicine.api.controller;

import homedoctor.medicine.api.dto.DefaultResponse;
import homedoctor.medicine.api.dto.alarm.request.CreateAlarmRequest;
import homedoctor.medicine.api.dto.alarm.request.UpdateAlarmRequest;
import homedoctor.medicine.api.dto.alarm.*;
import homedoctor.medicine.common.auth.Auth;
import homedoctor.medicine.domain.Alarm;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.service.AlarmService;
import homedoctor.medicine.service.JwtService;
import homedoctor.medicine.service.UserService;
import homedoctor.medicine.common.ResponseMessage;
import homedoctor.medicine.common.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AlarmApiController {

    private final AlarmService alarmService;

    private final UserService userService;

    private final JwtService jwtService;

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
            Long userId = jwtService.decode(header);
            User findUser = (User) userService.findOneById(userId).getData();

            if (request.validProperties()) {
                Alarm alarm = Alarm.builder()
                        .user(findUser)
                        .title(request.getTitle())
                        .label(request.getLabel())
                        .startDate(request.getStartDate())
                        .endDate(request.getEndDate())
                        .times(request.getTimes())
                        .repeats(request.getRepeats())
                        .alarmStatus(request.getAlarmStatus())
                        .build();
                DefaultResponse response = alarmService.save(alarm);
                return DefaultResponse.response(response.getStatus(),
                        response.getMessage());
            }
            return DefaultResponse.response(StatusCode.BAD_REQUEST,
                    ResponseMessage.ALARM_CREATE_FAIL);

        } catch (HttpMessageNotReadableException ex) {
            log.error(ex.getMessage());
            return DefaultResponse.response(StatusCode.BAD_REQUEST,
                    ResponseMessage.BAD_REQUEST);

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

            AlarmDto alarmDto = AlarmDto.builder()
                    .user(alarm.getUser().getId())
                    .alarmId(alarm.getId())
                    .title(alarm.getTitle())
                    .label(alarm.getLabel().getId())
                    .startDate(alarm.getStartDate())
                    .endDate(alarm.getEndDate())
                    .alarmStatus(alarm.getAlarmStatus())
                    .repeats(alarm.getRepeats())
                    .times(alarm.getTimes())
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
            List<AlarmDto> alarmDtoList = findAllAlarm.stream()
                    .map(m -> AlarmDto.builder()
                            .user(m.getUser().getId())
                            .alarmId(m.getId())
                            .title(m.getTitle())
                            .label(m.getLabel().getId())
                            .startDate(m.getStartDate())
                            .endDate(m.getEndDate())
                            .alarmStatus(m.getAlarmStatus())
                            .repeats(m.getRepeats())
                            .times(m.getTimes())
                            .build())
                    .collect(Collectors.toList());
            // 노출되는 알람 입력 정보중 필요한 필드만 가져오기.(엔티티 노출하지 않기!!)

            return DefaultResponse.response(response.getStatus(),
                    response.getMessage(),
                    alarmDtoList);
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
            List<AlarmDto> alarmDtoList = findEnableAlarmList.stream()
                    .map(m -> AlarmDto.builder()
                            .user(m.getUser().getId())
                            .alarmId(m.getId())
                            .title(m.getTitle())
                            .label(m.getLabel().getId())
                            .startDate(m.getStartDate())
                            .endDate(m.getEndDate())
                            .alarmStatus(m.getAlarmStatus())
                            .repeats(m.getRepeats())
                            .times(m.getTimes())
                            .build())
                    .collect(Collectors.toList());

            return DefaultResponse.response(response.getStatus(),
                    response.getMessage(),
                    alarmDtoList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @PutMapping("/alarm/{alarm_id}/edit")
    public DefaultResponse getUpdateAlarm(
            @RequestHeader("Authorization") final String header,
            @PathVariable("alarm_id") Long alarmId,
            @RequestBody @Valid UpdateAlarmRequest request) {
        try {
            if (header == null) {
                DefaultResponse.response(StatusCode.UNAUTHORIZED,
                        ResponseMessage.UNAUTHORIZED);
            }

            if (request.validProperties()) {
                Alarm alarm = Alarm.builder()
                        .title(request.getTitle())
                        .label(request.getLabel())
                        .startDate(request.getStartDate())
                        .endDate(request.getEndDate())
                        .alarmStatus(request.getAlarmStatus())
                        .times(request.getTimes())
                        .repeats(request.getRepeats())
                        .build();

                DefaultResponse response = alarmService.update(alarmId, alarm);
                return DefaultResponse.response(response.getStatus(),
                        response.getMessage());
            }

            return DefaultResponse.builder()
                    .status(StatusCode.BAD_REQUEST)
                    .message(ResponseMessage.NOT_CONTENT)
                    .build();
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
            DefaultResponse deleteResponse = alarmService.delete(findAlarm);

            return DefaultResponse.response(deleteResponse.getStatus(),
                    deleteResponse.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
