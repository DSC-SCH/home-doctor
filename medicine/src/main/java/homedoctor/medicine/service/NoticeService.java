package homedoctor.medicine.service;

import homedoctor.medicine.api.dto.DefaultResponse;
import homedoctor.medicine.common.ResponseMessage;
import homedoctor.medicine.common.StatusCode;
import homedoctor.medicine.domain.Notice;
import homedoctor.medicine.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;


    public DefaultResponse findOneNotice(Long noticeId) {
        try {
            Notice notice = noticeRepository.findOneNotice(noticeId);

            if (notice == null) {
                return DefaultResponse.response(StatusCode.BAD_REQUEST,
                        ResponseMessage.NOT_FOUND_NOTICE);
            }


            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.FOUND_NOTICE,
                    notice);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));

            return DefaultResponse.response(StatusCode.DB_ERROR,
                    ResponseMessage.DB_ERROR);
        }
    }

    public DefaultResponse findAllNotice() {
        try {
            List<Notice> noticeList = noticeRepository.findAllNotice();

            if (noticeList == null || noticeList.isEmpty()) {
                return DefaultResponse.response(StatusCode.BAD_REQUEST,
                        ResponseMessage.NOT_FOUND_NOTICE);
            }


            return DefaultResponse.response(StatusCode.OK,
                    ResponseMessage.FOUND_NOTICE,
                    noticeList);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));

            return DefaultResponse.response(StatusCode.DB_ERROR,
                    ResponseMessage.DB_ERROR);
        }
    }
}
