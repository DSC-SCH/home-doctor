package homedoctor.medicine.api.dto.connection;

import homedoctor.medicine.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CreateConnectionRequest {

    private User manager;
    private User receiver;

    public final boolean validProperties() {
        if (manager != null && receiver != null) {
            return true;
        }
        return false;
    }
}
