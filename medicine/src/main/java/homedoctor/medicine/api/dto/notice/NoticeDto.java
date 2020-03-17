package homedoctor.medicine.api.dto.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Data
@AllArgsConstructor
@Builder @Getter
public class NoticeDto {

    private Long noticeId;

    private String title;

    private String content;

    private Date createdDate;
}
