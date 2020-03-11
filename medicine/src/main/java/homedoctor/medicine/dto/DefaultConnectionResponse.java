package homedoctor.medicine.dto;

import homedoctor.medicine.domain.ConnectionUser;
import homedoctor.medicine.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class DefaultConnectionResponse {

    private int status;

    private String responseMessage;

    private ConnectionUser connectionUser;

    private User manager;
    private List<User> managerList;

    private User receiver;
    private List<User> receiverList;
}
