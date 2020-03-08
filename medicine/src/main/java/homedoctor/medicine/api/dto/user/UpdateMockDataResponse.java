package homedoctor.medicine.api.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class UpdateMockDataResponse {

    private Long id;

    private String username;

    private String street;

    private String phoneNumber;

}
