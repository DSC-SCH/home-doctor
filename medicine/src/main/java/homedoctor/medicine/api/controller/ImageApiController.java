package homedoctor.medicine.api.controller;

import homedoctor.medicine.api.dto.DefaultApiResponse;
import homedoctor.medicine.api.dto.image.CreateImageRequest;
import homedoctor.medicine.api.dto.image.ImageDto;
import homedoctor.medicine.common.ResponseMessage;
import homedoctor.medicine.common.StatusCode;
import homedoctor.medicine.domain.PrescriptionImage;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.dto.DefaultImageResponse;
import homedoctor.medicine.service.PrescriptionImageService;
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

    @GetMapping("/image/{image_id}")
    public DefaultApiResponse findUserOne(
            @PathVariable("image_id") Long id) {
        try {
            DefaultImageResponse response = imageService.findOneImage(id);
            PrescriptionImage image = response.getImage();

            ImageDto imageDto = ImageDto.builder()
                    .id(image.getId())
                    .alarm(image.getAlarm())
                    .image(image.getImage())
                    .build();

            return DefaultApiResponse.response(response.getStatus(),
                    response.getResponseMessage(),
                    imageDto);
        } catch (Exception e) {
            log.error(e.getMessage());

            return DefaultApiResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/image/user")
    public DefaultApiResponse findAllImageByUser(User user) {
        try {
            DefaultImageResponse response = imageService.findImagesByUser(user);
            List<PrescriptionImage> images = response.getImageList();

            List<ImageDto> imageDtoList = images.stream()
                    .map(m -> ImageDto.builder()
                            .id(m.getId())
                            .image(m.getImage())
                            .alarm(m.getAlarm())
                            .build())
                    .collect(Collectors.toList());

            return DefaultApiResponse.response(response.getStatus(),
                    response.getResponseMessage(),
                    imageDtoList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultApiResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/image/new")
    public DefaultApiResponse saveImage(
            @RequestBody @Valid CreateImageRequest request) {
        try {

            CreateImageRequest imageRequest = CreateImageRequest.builder()
                    .image(request.getImage())
                    .alarm(request.getAlarm())
                    .build();

            DefaultImageResponse response = imageService.save(imageRequest);

            return DefaultApiResponse.response(response.getStatus(),
                    response.getResponseMessage(),
                    imageRequest);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultApiResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/image/{image_id}")
    public DefaultApiResponse deleteImageResponse(
            @PathVariable("image_id") Long id) {
        try {
            DefaultImageResponse response = imageService.findOneImage(id);
            PrescriptionImage image = response.getImage();
            imageService.delete(image);

            return DefaultApiResponse.response(response.getStatus(),
                    response.getResponseMessage(),
                    id);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultApiResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
