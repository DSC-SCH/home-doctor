package homedoctor.medicine.dto;

import homedoctor.medicine.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class DefaultUserResponse {

    private int status;

    private String responseMessgae;

    private User user;

    private List<User> userList;
}
