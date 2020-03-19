package homedoctor.medicine.api.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Builder @Getter
public class UserCreateDto {

    private Long userId;

    private String token;
}
