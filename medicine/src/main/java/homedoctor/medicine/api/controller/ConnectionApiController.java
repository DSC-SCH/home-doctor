package homedoctor.medicine.api.controller;


import homedoctor.medicine.api.dto.DefaultResponse;
import homedoctor.medicine.api.dto.connection.ConnectionUserDto;
import homedoctor.medicine.api.dto.connection.request.CreateConnectionRequest;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.dto.DefaultConnectionResponse;
import homedoctor.medicine.service.ConnectionUserService;
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
public class ConnectionUserController {

    private final ConnectionUserService connectionUserService;

    private final UserService userService;

    @PostMapping("/connect/new")
    public DefaultResponse saveAlarm(
            User manager,
            User receiver,
            @RequestBody @Valid final CreateConnectionRequest request) {
        try {
            CreateConnectionRequest connection = CreateConnectionRequest.builder()
                    .manager(manager)
                    .receiver(receiver)
                    .build();

            DefaultConnectionResponse defaultAlarmResponse = connectionUserService.save(connection);
            return DefaultResponse.response(defaultAlarmResponse.getStatus(),
                    defaultAlarmResponse.getResponseMessage(),
                    connection);

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

    @GetMapping("/connect/manager")
    public DefaultResponse getManagerList(User user) {
        try {
            DefaultConnectionResponse defaultConnectResponse = connectionUserService.findALlManagerByUser(user);
            List<User> findManagerList = defaultConnectResponse.getManagerList();

            List<ConnectionUserDto> managerDtoList = findManagerList.stream()
                    .map(m -> ConnectionUserDto.builder()
                            .connectionId(m.getId())
                            .manager(m)
                            .build())
                    .collect(Collectors.toList());

            return DefaultResponse.response(defaultConnectResponse.getStatus(),
                    defaultConnectResponse.getResponseMessage(),
                    managerDtoList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/connect/receiver")
    public DefaultResponse getReceiverList(User user) {
        try {
            DefaultConnectionResponse defaultConnectResponse = connectionUserService.findAllReceiverByUser(user);
            List<User> findReceiverList = defaultConnectResponse.getReceiverList();

            List<ConnectionUserDto> managerDtoList = findReceiverList.stream()
                    .map(m -> ConnectionUserDto.builder()
                            .connectionId(m.getId())
                            .manager(m)
                            .build())
                    .collect(Collectors.toList());

            return DefaultResponse.response(defaultConnectResponse.getStatus(),
                    defaultConnectResponse.getResponseMessage(),
                    managerDtoList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/connect/{connect_id}")
    public DefaultResponse deleteConnectionResponse(
            @PathVariable("connect_id") Long id) {
        try {
            DefaultConnectionResponse response = connectionUserService.findConnectionById(id);
            connectionUserService.delete(response.getConnectionUser());

            return DefaultResponse.response(response.getStatus(),
                    response.getResponseMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
