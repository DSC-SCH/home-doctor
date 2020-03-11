package homedoctor.medicine.api.dto.code;

import homedoctor.medicine.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Builder
@Getter
public class CodeDto {

    private Long id;

    private Long user;

    private String code;

    private Integer life;
}
