package homedoctor.medicine.api.dto.code;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor @Builder
public class ResponseCode {

    private Long userId;
    private String code;
}
