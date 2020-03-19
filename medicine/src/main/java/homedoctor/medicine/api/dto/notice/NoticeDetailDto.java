package homedoctor.medicine.api.dto.notice;

import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;

@Data
@Getter
@AllArgsConstructor
@Builder
public class NoticeDetailDto {

    private String title;
    private String content;

    private Date createdDate;
}
