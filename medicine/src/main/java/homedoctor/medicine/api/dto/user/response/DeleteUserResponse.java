package homedoctor.medicine.api.dto.user.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteUserResponse {

    private Long id;

    private String username;

}
