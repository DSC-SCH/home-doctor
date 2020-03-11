package homedoctor.medicine.api.controller;

import homedoctor.medicine.api.dto.DefaultResponse;
import homedoctor.medicine.api.dto.image.CreateImageRequest;
import homedoctor.medicine.api.dto.image.imageAlarmRequest;
import homedoctor.medicine.api.dto.image.ImageDto;
import homedoctor.medicine.api.dto.image.UpdateImageRequest;
import homedoctor.medicine.common.ResponseMessage;
import homedoctor.medicine.common.StatusCode;
import homedoctor.medicine.common.auth.Auth;
import homedoctor.medicine.domain.Alarm;
import homedoctor.medicine.domain.PrescriptionImage;
import homedoctor.medicine.domain.User;
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

    @Auth
    @PostMapping("/image/new")
    public DefaultResponse saveImage(
            @RequestHeader("Authorization") final String header,
            @RequestBody @Valid CreateImageRequest request) {
        try {
            if (header == null) {
                return defaultHeaderResponse;
            }

            if (request.validProperties()) {
                for (String image : request.getImages()) {
                    Alarm findAlarm = (Alarm) alarmService.findAlarm(request.getAlarm()).getData();
                    PrescriptionImage prescriptionImage = PrescriptionImage.builder()
                            .image(image)
                            .alarm(findAlarm)
                            .build();

                    imageService.save(prescriptionImage);
                }

                return DefaultResponse.response(StatusCode.OK,
                        ResponseMessage.PRESCRIPTION_CREATE_SUCCESS);
            }

            return DefaultResponse.response(StatusCode.BAD_REQUEST,
                    ResponseMessage.PRESCRIPTION_CREATE_FAIL);

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
            System.out.println("=====Error========");
            System.out.println(id);

//            Alarm alarm = (Alarm) alarmService.findAlarm(request.getAlarmId()).getData();
            Alarm alarm = (Alarm) alarmService.findAlarm(id).getData();
            DefaultResponse response = imageService.findImagesByAlarm(alarm);
            List<PrescriptionImage> images = (List<PrescriptionImage>) response.getData();
            List<ImageDto> imageDtoList = images.stream()
                    .map(m -> ImageDto.builder()
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

            User findUser = (User) userService.findOneById(jwtService.decode(header)).getData();
            DefaultResponse response = imageService.findImagesByUser(findUser);
            List<PrescriptionImage> images = (List<PrescriptionImage>) response.getData();
            List<ImageDto> imageDtoList = images.stream()
                    .map(m -> ImageDto.builder()
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
    @DeleteMapping("/image/{image_id}")
    public DefaultResponse deleteImage(
            @RequestHeader("Authorization") final String header,
            @PathVariable("image_id") Long id) {
        try {
            if (header == null) {
                return defaultHeaderResponse;
            }
            DefaultResponse response = imageService.findOneImage(id);
            PrescriptionImage image = (PrescriptionImage) response.getData();
            imageService.delete(image);

            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.ALARM_DELETE_SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

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

            System.out.println("====errorTrack====");
            Alarm findAlarm = (Alarm) alarmService.findAlarm(request.getAlarm()).getData();
            List<PrescriptionImage> images =
                    (List<PrescriptionImage>) imageService.findImagesByAlarm(findAlarm).getData();

            // 해당 알람 이미지 삭제
            for (PrescriptionImage image : images) {
                imageService.delete(image);
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
}
