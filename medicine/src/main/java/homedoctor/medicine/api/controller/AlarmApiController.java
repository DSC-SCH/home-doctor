package homedoctor.medicine.api.controller;

import homedoctor.medicine.api.dto.DefaultApiResponse;
import homedoctor.medicine.api.dto.alarm.request.CreateAlarmRequest;
import homedoctor.medicine.api.dto.alarm.request.UpdateAlarmRequest;
import homedoctor.medicine.api.dto.alarm.*;
import homedoctor.medicine.common.auth.Auth;
import homedoctor.medicine.domain.Alarm;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.dto.DefaultAlarmResponse;
import homedoctor.medicine.service.AlarmService;
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

    // 예외 처리
    @GetMapping("/alarm/{alarm_id}")
    @Auth
    public DefaultApiResponse findOneAlarm(
            @PathVariable("alarm_id") Long id) {
        try {
            DefaultAlarmResponse response = alarmService.findAlarm(id);
            Alarm alarm = response.getAlarm();

            AlarmDto alarmDto = AlarmDto.builder()
                    .id(alarm.getId())
                    .title(alarm.getTitle())
                    .label(alarm.getLabel())
                    .startDate(alarm.getStartDate())
                    .endDate(alarm.getEndDate())
                    .alarmStatus(alarm.getAlarmStatus())
                    .repeats(alarm.getRepeats())
                    .times(alarm.getTimes())
                    .build();

            return DefaultApiResponse.response(response.getStatus(),
                    response.getResponseMessage(),
                    alarmDto);
        } catch (Exception e) {
            log.error(e.getMessage());

            return DefaultApiResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/alarm")
    public DefaultApiResponse getAllAlarmByUser(User user) {
        try {
            DefaultAlarmResponse response =
                    alarmService.findAlarmsByUser(user);

            List<Alarm> findAllAlarm = response.getAlarmList();
            List<AlarmDto> alarmDtoList = findAllAlarm.stream()
                    .map(m -> AlarmDto.builder()
                            .id(m.getId())
                            .title(m.getTitle())
                            .label(m.getLabel())
                            .startDate(m.getStartDate())
                            .endDate(m.getEndDate())
                            .alarmStatus(m.getAlarmStatus())
                            .repeats(m.getRepeats())
                            .times(m.getTimes())
                            .build())
                    .collect(Collectors.toList());
            // 노출되는 알람 입력 정보중 필요한 필드만 가져오기.(엔티티 노출하지 않기!!)

            return DefaultApiResponse.response(response.getStatus(),
                    response.getResponseMessage(),
                    AlarmAllResult.builder()
                                .data(alarmDtoList)
                                .counts(alarmDtoList.size())
                                .build());
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultApiResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/alarm/enable")
    public DefaultApiResponse getEnableAlarm(User user) {
        try {
            DefaultAlarmResponse defaultAlarmResponse = alarmService.findEnableAlarm(user);
            List<Alarm> findEnableAlarmList = defaultAlarmResponse.getAlarmList();

            List<AlarmDto> alarmDtoList = findEnableAlarmList.stream()
                    .map(m -> AlarmDto.builder()
                            .id(m.getId())
                            .title(m.getTitle())
                            .label(m.getLabel())
                            .startDate(m.getStartDate())
                            .endDate(m.getEndDate())
                            .alarmStatus(m.getAlarmStatus())
                            .repeats(m.getRepeats())
                            .times(m.getTimes())
                            .build())
                    .collect(Collectors.toList());

            return DefaultApiResponse.response(StatusCode.OK,
                    ResponseMessage.ALARM_SEARCH_SUCCESS,
                    alarmDtoList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultApiResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/alarm/new")
    public DefaultApiResponse saveAlarm(
            User user,
            @RequestBody @Valid final CreateAlarmRequest request) {
        try {
            CreateAlarmRequest createAlarmReq = CreateAlarmRequest.builder()
                    .user(user)
                    .title(request.getTitle())
                    .label(request.getLabel())
                    .startDate(request.getStartDate())
                    .endDate(request.getEndDate())
                    .times(request.getTimes())
                    .repeats(request.getRepeats())
                    .alarmStatus(request.getAlarmStatus())
                    .build();

            DefaultAlarmResponse response = alarmService.save(createAlarmReq);
            return DefaultApiResponse.response(response.getStatus(),
                    response.getResponseMessage(),
                    createAlarmReq);

        } catch (HttpMessageNotReadableException ex) {
            log.error(ex.getMessage());
            return DefaultApiResponse.response(StatusCode.BAD_REQUEST,
                    ResponseMessage.BAD_REQUEST);

        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultApiResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/alarm/{alarm_id}/edit")
    public DefaultApiResponse updateAlarm(
            @PathVariable("alarm_id") Long alarmId,
            @RequestBody @Valid UpdateAlarmRequest request) {
        try {
            UpdateAlarmRequest updateAlarmRequest = UpdateAlarmRequest.builder()
                    .title(request.getTitle())
                    .label(request.getLabel())
                    .startDate(request.getStartDate())
                    .endDate(request.getEndDate())
                    .alarmStatus(request.getAlarmStatus())
                    .times(request.getTimes())
                    .repeats(request.getRepeats())
                    .build();

            DefaultAlarmResponse response = alarmService.update(alarmId, updateAlarmRequest);
            return DefaultApiResponse.response(response.getStatus(),
                    response.getResponseMessage(),
                    updateAlarmRequest);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultApiResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/alarm/{alarm_id}")
    public DefaultApiResponse deleteAlarmResponse(
            @PathVariable("alarm_id") Long id) {
        try {
            DefaultAlarmResponse response = alarmService.findAlarm(id);
            Alarm findAlarm = response.getAlarm();
            alarmService.delete(findAlarm);

            return DefaultApiResponse.response(response.getStatus(),
                    response.getResponseMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultApiResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
