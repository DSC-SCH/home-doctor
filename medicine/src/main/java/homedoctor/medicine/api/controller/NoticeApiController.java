package homedoctor.medicine.api.controller;

import homedoctor.medicine.api.dto.DefaultResponse;
import homedoctor.medicine.api.dto.notice.NoticeDetailDto;
import homedoctor.medicine.api.dto.notice.NoticeDto;
import homedoctor.medicine.common.ResponseMessage;
import homedoctor.medicine.common.StatusCode;
import homedoctor.medicine.domain.Notice;
import homedoctor.medicine.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NoticeApiController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping("/notice/{notice_id}")
    public DefaultResponse getNoticeDetail(
            @PathVariable("notice_id") Long noticeId) {
        try {
            DefaultResponse response = noticeService.findOneNotice(noticeId);
            Notice notice = (Notice) response.getData();

            NoticeDetailDto dto = NoticeDetailDto.builder()
                    .title(notice.getTitle())
                    .content(notice.getContent())
                    .createdDate(notice.getCreatedDate())
                    .build();


            return DefaultResponse.response(response.getStatus(),
                    response.getMessage(), dto);

        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/notice")
    public DefaultResponse getNoticeAll() {
        try {
            DefaultResponse response = noticeService.findAllNotice();
            List<Notice> noticeList = (List<Notice>) response.getData();

            if (noticeList == null) {
                return DefaultResponse.response(StatusCode.BAD_REQUEST,
                        ResponseMessage.NOT_FOUND_NOTICE);
            }

            List<NoticeDto> dtoList = noticeList.stream()
            .map(m -> NoticeDto.builder()
                    .noticeId(m.getNoticeId())
                    .title(m.getTitle())
                    .content(m.getContent())
                    .createdDate(m.getCreatedDate())
                    .build())
                    .collect(Collectors.toList());

            return DefaultResponse.response(response.getStatus(),
                    response.getMessage(), dtoList);

        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
            return DefaultResponse.response(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
