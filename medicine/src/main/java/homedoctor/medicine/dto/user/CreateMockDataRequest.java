package homedoctor.medicine.dto.user;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestMapping;

@Data
@Getter @Setter
public class CreateMockDataRequest {

    private String username;

    private String street;

    private String phoneNumber;

}
