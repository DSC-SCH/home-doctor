package homedoctor.medicine.api.dto.user;

import homedoctor.medicine.domain.GenderType;
import homedoctor.medicine.domain.SnsType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder @Getter
@AllArgsConstructor
public class UserInfoResponse {

    private String username;

    private String birthday;

    private String email;

    private String snsId;

    private SnsType snsType;

    private GenderType genderType;

    private String phoneNum;
}
