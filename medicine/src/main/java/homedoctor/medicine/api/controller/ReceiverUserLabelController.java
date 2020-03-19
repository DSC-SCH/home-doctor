package homedoctor.medicine.api.controller;

import homedoctor.medicine.api.dto.DefaultResponse;
import homedoctor.medicine.api.dto.label.CreateLabelRequest;
import homedoctor.medicine.api.dto.label.LabelDto;
import homedoctor.medicine.common.ResponseMessage;
import homedoctor.medicine.common.StatusCode;
import homedoctor.medicine.common.auth.Auth;
import homedoctor.medicine.domain.Label;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.service.AlarmService;
import homedoctor.medicine.service.JwtService;
import homedoctor.medicine.service.LabelService;
import homedoctor.medicine.service.UserService;
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
public class ReceiverUserLabelController {

    private final LabelService labelService;

    private final UserService userService;

    private final JwtService jwtService;

    private final AlarmService alarmService;

    private final DefaultResponse defaultUnAuth =
            DefaultResponse.response(StatusCode.UNAUTHORIZED,
                    ResponseMessage.UNAUTHORIZED);


    @Auth
    @PostMapping("/label/{careUser_id}")
    public DefaultResponse saveLabel(
            @RequestHeader("Authorization") final String header,
            @RequestBody @Valid final CreateLabelRequest request,
            @PathVariable("careUser_id") final Long careUserId) {
        try {
            if (header == null) {
                return defaultUnAuth;
            }
            User findUser = (User) userService.findOneById(careUserId).getData();

            if (!request.validProperties()) {
                return DefaultResponse.response(StatusCode.BAD_REQUEST,
                        ResponseMessage.NOT_CONTENT);
            }

            Label label = Label.builder()
                    .user(findUser)
                    .title(request.getTitle())
                    .color(request.getColor())
                    .build();
            DefaultResponse response = labelService.save(label);

            return DefaultResponse.response(response.getStatus(),
                    response.getMessage());

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
    @GetMapping("/label/connect/{careUser_id}")
    public DefaultResponse getCareUserLabelByUser(
            @RequestHeader("Authorization") final String header,
            @PathVariable("careUser_id") @Valid final Long careUserId) {
        try {
            if (header == null) {
                return defaultUnAuth;
            }

            User findUser = (User) userService.findOneById(careUserId).getData();
            DefaultResponse response = labelService.fineLabelsByUser(findUser);
            List<Label> labels = (List<Label>) response.getData();

            if (labels == null) {
                String[] empty = new String[0];
                return DefaultResponse.response(StatusCode.OK,
                        ResponseMessage.NOT_FOUND_LABEL,
                        empty);
            }

            List<LabelDto> alarmDtoList = labels.stream()
                    .map(m -> LabelDto.builder()
                            .labelId(m.getId())
                            .user(m.getUser().getId())
                            .title(m.getTitle())
                            .color(m.getColor())
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
}
