package homedoctor.medicine.api.dto.user;

import homedoctor.medicine.domain.GenderType;
import homedoctor.medicine.domain.SnsType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;

    private String username;

    private String birthday;

    private String email;

    private SnsType snsType;

    private String token;

    private GenderType genderType;

    private String phoneNum;
}
