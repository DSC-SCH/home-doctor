package homedoctor.medicine.api.controller;


import homedoctor.medicine.api.dto.DefaultResponse;
import homedoctor.medicine.api.dto.alarm.AlarmDto;
import homedoctor.medicine.api.dto.alarm.ReceiverAlarmRequest;
import homedoctor.medicine.api.dto.connection.ConnectionUserDto;
import homedoctor.medicine.api.dto.connection.CreateConnectionRequest;
import homedoctor.medicine.common.auth.Auth;
import homedoctor.medicine.domain.Alarm;
import homedoctor.medicine.domain.ConnectionUser;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.service.AlarmService;
import homedoctor.medicine.service.ConnectionUserService;
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
public class ConnectionApiController {

    private final DefaultResponse unAuthorizeResponse =
            DefaultResponse.response(StatusCode.UNAUTHORIZED,
                    ResponseMessage.UNAUTHORIZED);

    private final ConnectionUserService connectionUserService;

    private final UserService userService;

    private final JwtService jwtService;

    private final AlarmService alarmService;

    @PostMapping("/connect/new")
    public DefaultResponse connectNew(
            User manager,
            User receiver,
            @RequestBody @Valid final CreateConnectionRequest request) {
        try {
            if (request.validProperties()) {
                ConnectionUser connection = ConnectionUser.builder()
                        .user(manager)
                        .careUser(receiver)
                        .build();

                DefaultResponse response = connectionUserService.save(connection);
                return DefaultResponse.response(response.getStatus(),
                        response.getMessage(),
                        connection);
            }

            return DefaultResponse.response(StatusCode.CONFLICT,
                    ResponseMessage.CONNECTION_CREATE_FAIL);

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
    @GetMapping("/connect/manager")
    public DefaultResponse getManagerList(
            @RequestHeader("Authorization") final String header) {
        try {
            if (header == null) {
                return unAuthorizeResponse;
            }
            User findUser = (User) userService.findOneById(jwtService.decode(header)).getData();
            DefaultResponse response = connectionUserService.findALlManagerByUser(findUser);
            List<User> findManagerList = (List<User>) response.getData();

            List<ConnectionUserDto> managerDtoList = findManagerList.stream()
                    .map(m -> ConnectionUserDto.builder()
                            .connectionId(m.getId())
                            .user(m.getId())
                            .build())
                    .collect(Collectors.toList());

            return DefaultResponse.response(response.getStatus(),
                    response.getMessage(),
                    managerDtoList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @GetMapping("/connect/receiver")
    public DefaultResponse getReceiverList(
            @RequestHeader("Authorization") final String header) {
        try {
            if (header == null) {
                return unAuthorizeResponse;
            }

            User findUser = (User) userService.findOneById(jwtService.decode(header)).getData();
            DefaultResponse response = connectionUserService.findAllReceiverByUser(findUser);
            List<User> findReceiverList = (List<User>) response.getData();
            List<ConnectionUserDto> managerDtoList = findReceiverList.stream()
                    .map(m -> ConnectionUserDto.builder()
                            .connectionId(m.getId())
                            .user(m.getId())
                            .build())
                    .collect(Collectors.toList());

            return DefaultResponse.response(response.getStatus(),
                    response.getMessage(),
                    managerDtoList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @GetMapping("/connect/receiver/alarm")
    public DefaultResponse getReceiverAlarm(
            @RequestHeader("Authorization") final String header,
            ReceiverAlarmRequest receiverAlarmRequest) {
        try {
            if (header == null) {
                return unAuthorizeResponse;
            }

            User receiverUser = (User) userService.findOneById(receiverAlarmRequest.getUser()).getData();
            List<Alarm> findReceiverAlarm = (List<Alarm>) alarmService.findEnableAlarm(receiverUser).getData();
            List<AlarmDto> managerDtoList = findReceiverAlarm.stream()
                    .map(m -> AlarmDto.builder()
                            .alarmId(m.getId())
                            .title(m.getTitle())
                            .user(m.getUser().getId())
                            .label(m.getLabel().getId())
                            .startDate(m.getStartDate())
                            .endDate(m.getEndDate())
                            .alarmStatus(m.getAlarmStatus())
                            .repeats(m.getRepeats())
                            .times(m.getTimes())
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

    @DeleteMapping("/connect/{connect_id}")
    public DefaultResponse cancelConnection(
            @PathVariable("connect_id") Long id) {
        try {
            DefaultResponse response = connectionUserService.findConnectionById(id);
            if (response.getData() != null) {
                return DefaultResponse.response(response.getStatus(),
                        response.getMessage());
            }

            DefaultResponse deleteResponse = connectionUserService.delete((ConnectionUser) response.getData());
            return DefaultResponse.response(deleteResponse.getStatus(),
                    deleteResponse.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
