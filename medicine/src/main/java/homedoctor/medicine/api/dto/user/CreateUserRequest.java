package homedoctor.medicine.api.dto.user;


import homedoctor.medicine.domain.GenderType;
import homedoctor.medicine.domain.SnsType;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Builder
@AllArgsConstructor
public class CreateUserRequest {

    private String username;

    private String birthday;

    private String email;

    private String snsId;

    private SnsType snsType;

    private GenderType genderType;

    private String phoneNum;


    public final boolean validProperties() {
        if (username != null && birthday != null && email != null &&
        email != null && snsId != null && snsType != null && genderType != null &&
        phoneNum != null) {
            return true;
        }
        return false;
    }
}
