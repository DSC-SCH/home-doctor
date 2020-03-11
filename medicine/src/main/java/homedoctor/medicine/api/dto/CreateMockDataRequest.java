package homedoctor.medicine.api.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter @Setter
public class CreateMockDataRequest {

    private String username;

    private String street;

    private String phoneNumber;

}
