package homedoctor.medicine.api.controller;

import com.sun.org.apache.bcel.internal.generic.ALOAD;
import homedoctor.medicine.api.dto.DefaultResponse;
import homedoctor.medicine.api.dto.alarm.AlarmDto;
import homedoctor.medicine.api.dto.alarm.ReceiverAlarmRequest;
import homedoctor.medicine.api.dto.alarm.UpdateAlarmRequest;
import homedoctor.medicine.api.dto.receiver.UpdateReceiverAlarm;
import homedoctor.medicine.api.dto.receiver.createReceiverAlarmRequest;
import homedoctor.medicine.common.ResponseMessage;
import homedoctor.medicine.common.StatusCode;
import homedoctor.medicine.common.auth.Auth;
import homedoctor.medicine.domain.Alarm;
import homedoctor.medicine.domain.Label;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.el.parser.AstLambdaExpression;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReceiverUserAlarmController {

    private final DefaultResponse unAuthorizeResponse =
            DefaultResponse.response(StatusCode.UNAUTHORIZED,
                    ResponseMessage.UNAUTHORIZED);

    private final ConnectionUserService connectionUserService;

    private final UserService userService;

    private final JwtService jwtService;

    private final AlarmService alarmService;

    private final LabelService labelService;

    private final ConnectionCodeService connectionCodeService;

    @Auth
    @PostMapping("/connect/receiver/alarm/{care_user_id}")
    public DefaultResponse createReceiverAlarm(
            @RequestHeader("Authorization") final String header,
            @RequestBody createReceiverAlarmRequest request,
            @PathVariable("care_user_id") final Long careUserId) {
        try {
            if (header == null) {
                return unAuthorizeResponse;
            }

            if (!request.validProperties()) {
                return DefaultResponse.response(StatusCode.BAD_REQUEST,
                        ResponseMessage.NOT_CONTENT);
            }

            User careUser = (User) userService.findOneById(careUserId).getData();
            Label careUserLabel = (Label) labelService.findOne(request.getLabel()).getData();

            Alarm alarm = Alarm.builder()
                    .user(careUser)
                    .title(request.getTitle())
                    .label(careUserLabel)
                    .startDate(request.getStartDate())
                    .endDate(request.getEndDate())
                    .times(request.getTimes())
                    .repeats(request.getRepeats())
                    .alarmStatus(request.getAlarmStatus())
                    .build();

            alarmService.save(alarm);
            AlarmDto alarmDto = AlarmDto.builder()
                    .alarmId(alarm.getId())
                    .build();

            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.ALARM_CREATE_SUCCESS,
                    alarmDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @GetMapping("/connect/receiver/alarm/{care_user_id}")
    public DefaultResponse getAllReceiverAlarm(
            @RequestHeader("Authorization") final String header,
            @PathVariable("care_user_id") final Long careUserId) {
        try {
            if (header == null) {
                return unAuthorizeResponse;
            }

            User receiverUser = (User) userService.findOneById(careUserId).getData();
            List<Alarm> findReceiverAlarm = (List<Alarm>) alarmService.findEnableAlarm(receiverUser).getData();

            if (findReceiverAlarm == null) {
                String[] empty = new String[0];
                return DefaultResponse.response(StatusCode.OK,
                        ResponseMessage.NOT_FOUND_ALARM, empty);
            }

            List<AlarmDto> managerDtoList = findReceiverAlarm.stream()
                    .map(m -> AlarmDto.builder()
                            .alarmId(m.getId())
                            .title(m.getTitle())
                            .user(m.getUser().getId())
                            .label(m.getLabel().getId())
                            .startDate(m.getStartDate())
                            .endDate(m.getEndDate())
                            .labelTitle(m.getLabel().getTitle())
                            .color(m.getLabel().getColor())
                            .alarmStatus(m.getAlarmStatus())
                            .repeats(m.getRepeats())
                            .times(m.getTimes())
                            .createdDate(m.getCreatedDate())
                            .lastModifiedDate(m.getLastModifiedDate())
                            .build())

                    .collect(Collectors.toList());

            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.ALARM_SEARCH_SUCCESS,
                    managerDtoList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @GetMapping("/connect/receiver/alarm/{care_user_id}/{alarm_id}")
    public DefaultResponse getOneReceiverAlarm(
            @RequestHeader("Authorization") final String header,
            @PathVariable("care_user_id") final Long careUserId,
            @PathVariable("alarm_id") final Long alarmId) {
        try {
            if (header == null) {
                return unAuthorizeResponse;
            }

            Alarm findReceiverAlarm = (Alarm) alarmService.findAlarm(alarmId).getData();

            if (findReceiverAlarm == null) {
                String[] empty = new String[0];
                return DefaultResponse.response(StatusCode.OK,
                        ResponseMessage.NOT_FOUND_ALARM, empty);
            }

            AlarmDto alarmDto = AlarmDto.builder()
                    .alarmId(findReceiverAlarm.getId())
                    .user(findReceiverAlarm.getUser().getId())
                    .title(findReceiverAlarm.getTitle())
                    .label(findReceiverAlarm.getLabel().getId())
                    .labelTitle(findReceiverAlarm.getLabel().getTitle())
                    .startDate(findReceiverAlarm.getStartDate())
                    .endDate(findReceiverAlarm.getEndDate())
                    .times(findReceiverAlarm.getTimes())
                    .repeats(findReceiverAlarm.getRepeats())
                    .alarmStatus(findReceiverAlarm.getAlarmStatus())
                    .build();

            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.ALARM_SEARCH_SUCCESS,
                    alarmDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }


    @Auth
    @PutMapping("/connect/receiver/alarm/{care_user_id}/{alarm_id}")
    public DefaultResponse updateReceiverAlarm(
            @RequestBody @Valid UpdateAlarmRequest request,
            @RequestHeader("Authorization") final String header,
            @PathVariable("care_user_id") final Long careUserId,
            @PathVariable("alarm_id") final Long alarmId) {
        try {
            if (header == null) {
                return unAuthorizeResponse;
            }

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

            DefaultResponse response = alarmService.update(alarmId, alarm);


            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.ALARM_UPDATE_SUCCESS);

        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }


    @Auth
    @DeleteMapping("/connect/receiver/alarm/{care_user_id}/{alarm_id}")
    public DefaultResponse deleteReceiverAlarm(
            @RequestHeader("Authorization") final String header,
            @PathVariable("care_user_id") final Long careUserId,
            @PathVariable("alarm_id") final Long alarmId) {
        try {
            if (header == null) {
                return unAuthorizeResponse;
            }

            DefaultResponse response = alarmService.findAlarm(alarmId);
            Alarm findAlarm = (Alarm) response.getData();

            if (findAlarm == null) {
                String[] empty = new String[0];

                return DefaultResponse.response(StatusCode.OK,
                        ResponseMessage.NOT_FOUND_ALARM,
                        empty);
            }

            DefaultResponse deleteAlarm = alarmService.delete(findAlarm);

            return DefaultResponse.response(deleteAlarm.getStatus(),
                    deleteAlarm.getMessage());

        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
