package homedoctor.medicine.dto;

import homedoctor.medicine.domain.ConnectionCode;
import homedoctor.medicine.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DefaultCodeResponse {

    private int status;

    private String responseMessage;

    private User createUser;

    private ConnectionCode connectionCode;

    private Integer life;
}
