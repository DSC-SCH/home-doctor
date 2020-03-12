package homedoctor.medicine.api.controller;

import homedoctor.medicine.api.dto.DefaultResponse;
import homedoctor.medicine.api.dto.code.CodeDto;
import homedoctor.medicine.api.dto.code.CreateCodeRequest;
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
    @PostMapping("/code/new")
    public DefaultResponse createCode(
            @RequestHeader("Authorization") final String header,
            @RequestBody @Valid final CreateCodeRequest request) {
        try {
            if (header == null) {
                return DefaultResponse.response(StatusCode.UNAUTHORIZED,
                        ResponseMessage.UNAUTHORIZED);
            }
            User findUser = (User) userService.findOneById(jwtService.decode(header)).getData();

            if (request.validProperties()) {
                ConnectionCode code = ConnectionCode.builder()
                        .user(findUser)
                        .code(request.getCode())
                        .life(request.getLife())
                        .build();
                DefaultResponse response = codeService.saveCode(code);

                return DefaultResponse.response(response.getStatus(),
                        response.getMessage());
            }
            return DefaultResponse.response(StatusCode.BAD_REQUEST,
                    ResponseMessage.CODE_CREATE_FAIL);

        } catch (Exception e) {
            log.error(e.getMessage());

            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @GetMapping("/code/{code_id}")
    public DefaultResponse getCode(
            @RequestHeader("Authorization") final String header,
            @PathVariable("code_id") Long id) {
        try {
            if (header == null) {
                return DefaultResponse.response(StatusCode.UNAUTHORIZED,
                        ResponseMessage.UNAUTHORIZED);
            }

            DefaultResponse response = codeService.findCode(id);
            ConnectionCode code = (ConnectionCode) response.getData();

            if (code == null) {
                return DefaultResponse.response(StatusCode.OK,
                        ResponseMessage.NOT_FOUND_CODE);
            }
            CodeDto codeDto = CodeDto.builder()
                    .id(id)
                    .user(code.getUser().getId())
                    .code(code.getCode())
                    .life(code.getLife())
                    .build();

            return DefaultResponse.response(response.getStatus(),
                    response.getMessage(),
                    codeDto);

        } catch (Exception e) {
            log.error(e.getMessage());

            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

}
