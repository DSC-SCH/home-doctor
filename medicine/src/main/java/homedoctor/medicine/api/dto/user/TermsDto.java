package homedoctor.medicine.api.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

@Data
@AllArgsConstructor
@Builder @Getter
public class TermsDto {

    private String title;

    private String content;
}
