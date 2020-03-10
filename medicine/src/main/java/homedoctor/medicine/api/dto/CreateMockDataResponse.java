package homedoctor.medicine.api.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter @Setter
public class CreateMockDataResponse {

    private Long id;
    private String username;
    private String street;
    private String phoneNumber;

    public CreateMockDataResponse(Long id, String username, String street, String phoneNumber) {
        this.id = id;
        this.username = username;
        this.street = street;
        this.phoneNumber = phoneNumber;
    }
}
