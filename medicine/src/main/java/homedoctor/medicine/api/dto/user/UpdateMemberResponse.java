package homedoctor.medicine.api.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class UpdateMemberResponse {
    private Long id;
    private String name;
}
