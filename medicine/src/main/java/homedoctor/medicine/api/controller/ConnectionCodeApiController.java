package homedoctor.medicine.api.controller;

import homedoctor.medicine.api.dto.DefaultApiResponse;
import homedoctor.medicine.api.dto.code.CodeDto;
import homedoctor.medicine.api.dto.code.request.CreateCodeRequest;
import homedoctor.medicine.domain.ConnectionCode;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.dto.DefaultCodeResponse;
import homedoctor.medicine.service.ConnectionCodeService;
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

    @GetMapping("/code/{code_id}")
    public DefaultApiResponse findOneAlarm(
            @PathVariable("code_id") Long id) {
        try {
            DefaultCodeResponse response = codeService.findCode(id);
            ConnectionCode code = response.getConnectionCode();

            CodeDto codeDto = CodeDto.builder()
                    .id(id)
                    .user(code.getUser())
                    .code(code.getCode())
                    .life(code.getLife())
                    .build();

            return DefaultApiResponse.response(response.getStatus(),
                    response.getResponseMessage(),
                    codeDto);

        } catch (Exception e) {
            log.error(e.getMessage());

            return DefaultApiResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/code/new")
    public DefaultApiResponse createCode(
            User user,
            @RequestBody @Valid final CreateCodeRequest request) {
        try {

            CreateCodeRequest code = CreateCodeRequest.builder()
                    .user(request.getUser())
                    .code(request.getCode())
                    .life(request.getLife())
                    .build();

            DefaultCodeResponse response = codeService.saveCode(request);

            return DefaultApiResponse.response(response.getStatus(),
                    response.getResponseMessage(),
                    code);

        } catch (Exception e) {
            log.error(e.getMessage());

            return DefaultApiResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
