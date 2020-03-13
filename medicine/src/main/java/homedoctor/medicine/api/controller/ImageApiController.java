package homedoctor.medicine.api.controller;

import homedoctor.medicine.api.dto.DefaultResponse;
import homedoctor.medicine.api.dto.image.*;
import homedoctor.medicine.common.ResponseMessage;
import homedoctor.medicine.common.StatusCode;
import homedoctor.medicine.common.auth.Auth;
import homedoctor.medicine.domain.Alarm;
import homedoctor.medicine.domain.PrescriptionImage;
import homedoctor.medicine.service.AlarmService;
import homedoctor.medicine.service.JwtService;
import homedoctor.medicine.service.PrescriptionImageService;
import homedoctor.medicine.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ImageApiController {

    private final PrescriptionImageService imageService;

    private final UserService userService;

    private final JwtService jwtService;

    private final AlarmService alarmService;

    private final DefaultResponse defaultHeaderResponse =
            DefaultResponse.response(StatusCode.UNAUTHORIZED, ResponseMessage.UNAUTHORIZED);

    // 알람 생성 직후 바로 알람 아이디를 넘겨서 이미지 저장.
    @Auth
    @PostMapping("/image/{alarm_id}")
    public DefaultResponse saveImage(
            @RequestHeader("Authorization") final String header,
            @PathVariable("alarm_id") final Long alarmId,
            @RequestBody @Valid CreateImageRequest request) {
        try {
            if (header == null) {
                return defaultHeaderResponse;
            }

            for (String image : request.getImage()) {
                Alarm findAlarm = (Alarm) alarmService.findAlarm(alarmId).getData();
                PrescriptionImage prescriptionImage = PrescriptionImage.builder()
                        .image(image)
                        .alarm(findAlarm)
                        .build();

                imageService.save(prescriptionImage);
            }

            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.PRESCRIPTION_CREATE_SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @GetMapping("/image/{image_id}")
    public DefaultResponse getImageOne(
            @RequestHeader("Authorization") final String header,
            @PathVariable("image_id") Long id) {
        try {
            if (header == null) {
                return defaultHeaderResponse;
            }
            DefaultResponse response = imageService.findOneImage(id);
            PrescriptionImage image = (PrescriptionImage) response.getData();

            if (image == null) {
                return DefaultResponse.response(StatusCode.OK,
                        ResponseMessage.NOT_FOUND_PRESCRIPTION);
            }

            ImageDto imageDto = ImageDto.builder()
                    .imageId(image.getId())
                    .alarm(image.getAlarm().getId())
                    .image(image.getImage())
                    .build();

            return DefaultResponse.response(response.getStatus(),
                    response.getMessage(),
                    imageDto);
        } catch (Exception e) {
            log.error(e.getMessage());

            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @GetMapping("/image/alarm/{alarm_id}")
    public DefaultResponse getImageByAlarm(
            @RequestHeader("Authorization") final String header,
            @PathVariable("alarm_id") Long id) {
        try {
            if (header == null) {
                return defaultHeaderResponse;
            }

//            Alarm alarm = (Alarm) alarmService.findAlarm(request.getAlarmId()).getData();
            DefaultResponse response = imageService.findImagesByAlarm(id);
            List<PrescriptionImage> images = (List<PrescriptionImage>) response.getData();


            if (images == null) {
                String[] empty = new String[0];
                return DefaultResponse.response(StatusCode.OK,
                        ResponseMessage.NOT_FOUND_PRESCRIPTION,
                        empty);
            }

            List<ImageByAlarmResponse> imageDtoList = images.stream()
                    .map(m -> ImageByAlarmResponse.builder()
                            .imageId(m.getId())
                            .image(m.getImage())
                            .alarm(m.getAlarm().getId())
                            .build())
                    .collect(Collectors.toList());

            return DefaultResponse.response(response.getStatus(),
                    response.getMessage(),
                    imageDtoList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @GetMapping("/image/user")
    public DefaultResponse getImageByUser(
            @RequestHeader("Authorization") final String header) {
        try {
            if (header == null) {
                return defaultHeaderResponse;
            }
            Long userId = jwtService.decode(header);
            DefaultResponse response = imageService.findImagesByUser(userId);
            List<PrescriptionImage> images = (List<PrescriptionImage>) response.getData();

            if (images == null) {
                String[] empty = new String[0];
                return DefaultResponse.response(StatusCode.OK,
                        ResponseMessage.NOT_FOUND_PRESCRIPTION,
                        empty);
            }

            List<ImageDto> imageDtoList = images.stream()
                    .map(m -> ImageDto.builder()
                            .imageId(m.getId())
                            .image(m.getImage())
                            .alarm(m.getAlarm().getId())
                            .alarmTitle(m.getAlarm().getTitle())
                            .labelColor(m.getAlarm().getLabel().getColor())
                            .build())
                    .collect(Collectors.toList());

            return DefaultResponse.response(response.getStatus(),
                    response.getMessage(),
                    imageDtoList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @PutMapping("/image/{alarm_id}")
    public DefaultResponse updateImageByAlarm(
            @RequestHeader("Authorization") final String header,
            @PathVariable("alarm_id") final Long alarmId,
            @RequestBody UpdateImageByAlarmRequest request) {
        try {
            if (header == null) {
                return defaultHeaderResponse;
            }

            if (request.getImage() == null || request.getImage().isEmpty()) {
                DefaultResponse.response(StatusCode.METHOD_NOT_ALLOWED,
                        ResponseMessage.PRESCRIPTION_UPDATE_FAIL);
            }

            imageService.updateImagesByAlarm(alarmId, request.getImage());

            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.PRESCRIPTION_UPDATE_SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    // 아직 사용 안하는 API
    @Auth
    @PutMapping("/image/edit")
    public DefaultResponse updateImage(
            @RequestHeader("Authorization") final String header,
            @RequestBody UpdateImageRequest request) {
        try {
            if (header == null) {
                return defaultHeaderResponse;
            }

            if (!request.validProperties()) {
                return DefaultResponse.response(StatusCode.METHOD_NOT_ALLOWED,
                        ResponseMessage.NOT_CONTENT);
            }

            if (request.getImages() == null) {
                DefaultResponse.response(StatusCode.METHOD_NOT_ALLOWED,
                        ResponseMessage.PRESCRIPTION_UPDATE_FAIL);
            }

            Alarm findAlarm = (Alarm) alarmService.findAlarm(request.getAlarm()).getData();
            Long alarmId = findAlarm.getId();

            List<PrescriptionImage> images =
                    (List<PrescriptionImage>) imageService.findImagesByAlarm(alarmId).getData();

            // 해당 알람 이미지 삭제
            for (PrescriptionImage image : images) {
                imageService.delete(image.getId());
            }

            // 요청 들어온 알람 등록
            for (String newImage : request.getImages()) {
                PrescriptionImage prescriptionImage = PrescriptionImage.builder()
                        .image(newImage)
                        .alarm(findAlarm)
                        .build();
                imageService.save(prescriptionImage);
            }

            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.PRESCRIPTION_UPDATE_SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @DeleteMapping("/image/{image_id}")
    public DefaultResponse deleteImage(
            @RequestHeader("Authorization") final String header,
            @PathVariable("image_id") Long id) {
        try {
            if (header == null) {
                return defaultHeaderResponse;
            }

            if (id == null) {
                String[] empty = new String[0];
                return DefaultResponse.response(StatusCode.OK,
                        ResponseMessage.NOT_FOUND_PRESCRIPTION);
            }

            imageService.delete(id);

            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.ALARM_DELETE_SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
