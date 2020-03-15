package homedoctor.medicine.api.controller;


import homedoctor.medicine.api.dto.DefaultResponse;
import homedoctor.medicine.api.dto.connection.ConnectionUserInfoResponse;
import homedoctor.medicine.api.dto.connection.CreateConnectionRequest;
import homedoctor.medicine.common.auth.Auth;
import homedoctor.medicine.domain.ConnectionCode;
import homedoctor.medicine.domain.ConnectionUser;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.service.*;
import homedoctor.medicine.common.ResponseMessage;
import homedoctor.medicine.common.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ConnectionUserApiController {

    private final DefaultResponse unAuthorizeResponse =
            DefaultResponse.response(StatusCode.UNAUTHORIZED,
                    ResponseMessage.UNAUTHORIZED);

    private final ConnectionUserService connectionUserService;

    private final UserService userService;

    private final JwtService jwtService;

    private final AlarmService alarmService;

    private final ConnectionCodeService connectionCodeService;

    @PostMapping("/connect/new")
    public DefaultResponse connectNew(
            @RequestBody @Valid final CreateConnectionRequest request) {
        try {
            boolean isSameCode = (boolean) connectionCodeService.isSameCode(request.getCode()).getData();

            // 코드 유효성 검사
            if (!isSameCode) {
                return DefaultResponse.response(StatusCode.BAD_REQUEST,
                        ResponseMessage.NOT_EQUAL_CODE);
            }

            // 코드 시간 검증
            Date currentDate = new Date();
            Calendar calendar = Calendar.getInstance();
            ConnectionCode findCode = (ConnectionCode) connectionCodeService.getCode(request.getCode()).getData();
            Date codeCreateTime = findCode.getLife();
            calendar.setTime(codeCreateTime);
            calendar.add(Calendar.MINUTE, 3);

            long diff = currentDate.getTime() - codeCreateTime.getTime();
            long sec = diff / 1000;

            // 3분 이상이면 코드 입력 유효시간 초과.
            if (sec > 180) {
                DefaultResponse.response(StatusCode.BAD_REQUEST,
                        ResponseMessage.VALID_TIMEOUT);
            }


            DefaultResponse response = connectionUserService.save(request.getManager(), findCode.getUser().getId());
            return DefaultResponse.response(response.getStatus(),
                    response.getMessage());

        } catch (HttpMessageNotReadableException ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
            return DefaultResponse.response(StatusCode.BAD_REQUEST,
                    ResponseMessage.BAD_REQUEST);

        } catch (Exception e) {
            e.printStackTrace();
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

            if (findManagerList == null) {
                String[] empty = new String[0];
                return DefaultResponse.response(StatusCode.OK,
                        ResponseMessage.NOT_FOUND_CONNECTION, empty);
            }

            List<ConnectionUserInfoResponse> managerDtoList = findManagerList.stream()
                    .map(m -> ConnectionUserInfoResponse.builder()
                            .connectionId(m.getId())
                            .username(m.getUsername())
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

            if (findReceiverList == null) {
                String[] empty = new String[0];
                return DefaultResponse.response(StatusCode.OK,
                        ResponseMessage.NOT_FOUND_CONNECTION, empty);
            }

            List<ConnectionUserInfoResponse> managerDtoList = findReceiverList.stream()
                    .map(m -> ConnectionUserInfoResponse.builder()
                            .username(m.getUsername())
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



    @DeleteMapping("/connect/{connect_id}")
    public DefaultResponse cancelConnection(
            @PathVariable("connect_id") Long id) {
        try {
            DefaultResponse response = connectionUserService.findConnectionById(id);
            if (response.getData() == null) {
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
