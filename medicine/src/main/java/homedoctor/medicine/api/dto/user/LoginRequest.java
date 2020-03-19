package homedoctor.medicine.api.dto.user;

import homedoctor.medicine.domain.SnsType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
public class LoginRequest {

    private SnsType snsType;

    private String snsId;
}
