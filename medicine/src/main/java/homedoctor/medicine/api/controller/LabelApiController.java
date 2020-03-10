package homedoctor.medicine.api.controller;

import homedoctor.medicine.api.dto.DefaultApiResponse;
import homedoctor.medicine.api.dto.label.LabelDto;
import homedoctor.medicine.api.dto.label.reqeust.CreateLabelRequest;
import homedoctor.medicine.api.dto.label.reqeust.UpdateLabelRequest;
import homedoctor.medicine.domain.Label;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.dto.DefaultLabelResponse;
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


    @GetMapping("/label/{label_id}")
    public DefaultApiResponse findOneLabel(
            @PathVariable("label_id") Long id){

        try {
            DefaultLabelResponse defaultLabelResponse = labelService.findOne(id);

            Label label = defaultLabelResponse.getLabel();

            LabelDto labelDto = LabelDto.builder()
                    .id(label.getId())
                    .user(label.getUser())
                    .title(label.getTitle())
                    .color(label.getColor())
                    .build();
            return DefaultApiResponse.response(defaultLabelResponse.getStatus(),
                    defaultLabelResponse.getResponseMessage(),
                    labelDto);
        } catch (Exception e) {
            log.error(e.getMessage());

            return DefaultApiResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/label")
    public DefaultApiResponse getAllLabelByUser(User user) {
        try {
            DefaultLabelResponse defaultLabelResponse = labelService.fineLabelsByUser(user);

            List<Label> labels = defaultLabelResponse.getLabelList();
            List<LabelDto> alarmDtoList = labels.stream()
                    .map(m -> LabelDto.builder()
                            .id(m.getId())
                            .user(m.getUser())
                            .title(m.getTitle())
                            .color(m.getColor())
                            .build())
                    .collect(Collectors.toList());


            return DefaultApiResponse.response(defaultLabelResponse.getStatus(),
                    defaultLabelResponse.getResponseMessage(),
                    alarmDtoList);

        } catch (Exception e) {
            log.error(e.getMessage());

            return DefaultApiResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/label/new")
    public DefaultApiResponse saveAlarm(
            User user,
            @RequestBody @Valid final CreateLabelRequest request) {
        try {
            CreateLabelRequest labelRequest = CreateLabelRequest.builder()
                    .user(user)
                    .title(request.getTitle())
                    .color(request.getColor())
                    .build();

            DefaultLabelResponse defaultAlarmResponse = labelService.saveLabel(labelRequest);

            return DefaultApiResponse.response(defaultAlarmResponse.getStatus(),
                    defaultAlarmResponse.getResponseMessage(),
                    labelRequest);

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


    @PostMapping("/label/{label_id}/edit")
    public DefaultApiResponse updateLabel(
            @PathVariable("label_id") Long labelId,
            @RequestBody @Valid UpdateLabelRequest request) {
        try {
            UpdateLabelRequest updateLabelRequest = UpdateLabelRequest.builder()
                    .id(labelId)
                    .title(request.getTitle())
                    .color(request.getColor())
                    .build();

            DefaultLabelResponse response = labelService.update(labelId, updateLabelRequest);
            return DefaultApiResponse.response(response.getStatus(),
                    response.getResponseMessage(),
                    updateLabelRequest);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultApiResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

}
