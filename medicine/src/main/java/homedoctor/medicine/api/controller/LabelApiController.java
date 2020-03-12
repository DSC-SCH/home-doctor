package homedoctor.medicine.api.controller;

import homedoctor.medicine.api.dto.DefaultResponse;
import homedoctor.medicine.api.dto.label.LabelDto;
import homedoctor.medicine.api.dto.label.CreateLabelRequest;
import homedoctor.medicine.api.dto.label.UpdateLabelRequest;
import homedoctor.medicine.common.auth.Auth;
import homedoctor.medicine.domain.Alarm;
import homedoctor.medicine.domain.Label;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.service.AlarmService;
import homedoctor.medicine.service.JwtService;
import homedoctor.medicine.service.LabelService;
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
public class LabelApiController {

    private final LabelService labelService;

    private final UserService userService;

    private final JwtService jwtService;

    private final AlarmService alarmService;

    private final DefaultResponse defaultUnAuth =
            DefaultResponse.response(StatusCode.UNAUTHORIZED,
                    ResponseMessage.UNAUTHORIZED);

    @Auth
    @PostMapping("/label/new")
    public DefaultResponse saveLabel(
            @RequestHeader("Authorization") final String header,
            @RequestBody @Valid final CreateLabelRequest request) {
        try {
            if (header == null) {
                return defaultUnAuth;
            }
            User findUser =
                    (User) userService.findOneById(jwtService.decode(header)).getData();

            if (request.validProperties()) {
                Label label = Label.builder()
                        .user(findUser)
                        .title(request.getTitle())
                        .color(request.getColor())
                        .build();
                DefaultResponse response = labelService.save(label);

                return DefaultResponse.response(response.getStatus(),
                        response.getMessage());
            }
            return DefaultResponse.response(StatusCode.BAD_REQUEST,
                    ResponseMessage.LABEL_CREATE_FAIL);

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
    @GetMapping("/label/{label_id}")
    public DefaultResponse getOneLabel(
            @RequestHeader("Authorization") final String header,
            @PathVariable("label_id") Long id){

        try {
            if (header == null) {
                return defaultUnAuth;
            }

            DefaultResponse response = labelService.findOne(id);
            Label label = (Label) response.getData();

            if (label == null) {
                return DefaultResponse.response(StatusCode.OK,
                        ResponseMessage.NOT_FOUND_LABEL);
            }

            LabelDto labelDto = LabelDto.builder()
                    .labelId(label.getId())
                    .user(label.getUser().getId())
                    .title(label.getTitle())
                    .color(label.getColor())
                    .build();
            return DefaultResponse.response(response.getStatus(),
                    response.getMessage(),
                    labelDto);
        } catch (Exception e) {
            log.error(e.getMessage());

            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @GetMapping("/label")
    public DefaultResponse getLabelByUser(
            @RequestHeader("Authorization") final String header) {
        try {
            if (header == null) {
                return defaultUnAuth;
            }
            User findUser = (User) userService.findOneById(jwtService.decode(header)).getData();
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

    @Auth
    @PutMapping("/label/{label_id}")
    public DefaultResponse updateLabel(
            @RequestHeader("Authorization") final String header,
            @PathVariable("label_id") Long labelId,
            @RequestBody @Valid UpdateLabelRequest request) {
        try {
            if (header == null) {
                return defaultUnAuth;
            }

            if (request.validProperties()) {
                Label label = Label.builder()
                        .title(request.getTitle())
                        .color(request.getColor())
                        .build();

                DefaultResponse response = labelService.update(labelId, label);
                return DefaultResponse.response(response.getStatus(),
                        response.getMessage());
            }

            return DefaultResponse.response(StatusCode.BAD_REQUEST,
                    ResponseMessage.LABEL_UPDATE_FAIL);

        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @DeleteMapping("/label/{label_id}")
    public DefaultResponse deleteLabel(
            @RequestHeader("Authorization") final String header,
            @PathVariable("label_id") Long labelId) {
        try {
            if (header == null) {
                return defaultUnAuth;
            }

            Long userId= jwtService.decode(header);
            alarmService.changeAlarmLabelIfDeleteLabel(userId, labelId);
            DefaultResponse response = labelService.delete(labelId);

            return DefaultResponse.response(response.getStatus(),
                    response.getMessage());

        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

}
