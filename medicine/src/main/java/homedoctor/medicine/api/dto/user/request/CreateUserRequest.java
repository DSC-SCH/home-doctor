package homedoctor.medicine.api.dto.user.request;


import homedoctor.medicine.domain.GenderType;
import homedoctor.medicine.domain.SnsType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class CreateUserRequest {

    private String username;

    private String birthday;

    private String email;

    private String kakaoId;

    private String googleId;

    private SnsType snsType;

    private GenderType genderType;

    private String phoneNum;
}
