package homedoctor.medicine.api.dto.connection;

import homedoctor.medicine.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CreateConnectionRequest {

    private String code;

    private Long manager;
}
