package homedoctor.medicine.api.dto.user.response;

import lombok.Data;

@Data
public class CreateUserResponse {
    public Long id;

    public CreateUserResponse(Long id) {
        this.id = id;
    }
}
