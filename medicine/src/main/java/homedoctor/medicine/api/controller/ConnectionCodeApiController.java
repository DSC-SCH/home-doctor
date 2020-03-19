package homedoctor.medicine.api.controller;

import homedoctor.medicine.api.dto.DefaultResponse;
import homedoctor.medicine.api.dto.code.CodeDto;
import homedoctor.medicine.api.dto.code.CreateCodeRequest;
import homedoctor.medicine.api.dto.code.ResponseCode;
import homedoctor.medicine.common.auth.Auth;
import homedoctor.medicine.domain.ConnectionCode;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.service.ConnectionCodeService;
import homedoctor.medicine.service.JwtService;
import homedoctor.medicine.service.UserService;
import homedoctor.medicine.common.ResponseMessage;
import homedoctor.medicine.common.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ConnectionCodeApiController {

    private final ConnectionCodeService codeService;

    private final UserService userService;

    private final JwtService jwtService;

    @Auth
    @PostMapping("/code")
    public DefaultResponse createCode(
            @RequestHeader("Authorization") final String header,
            @RequestBody @Valid final CreateCodeRequest request) {
        try {
            if (header == null) {
                return DefaultResponse.response(StatusCode.UNAUTHORIZED,
                        ResponseMessage.UNAUTHORIZED);
            }

            if (request == null) {
                return DefaultResponse.response(StatusCode.BAD_REQUEST,
                        ResponseMessage.CODE_CREATE_FAIL);
            }

            DefaultResponse response = codeService.saveCode(request.getUserId());
            ConnectionCode connectionCode = (ConnectionCode) response.getData();

            ResponseCode responseCode = ResponseCode.builder()
                    .code(connectionCode.getCode())
                    .userId(connectionCode.getUser().getId())
                    .build();

            return DefaultResponse.response(response.getStatus(),
                    response.getMessage(),
                    responseCode);

        } catch (Exception e) {
            log.error(e.getMessage());

            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
