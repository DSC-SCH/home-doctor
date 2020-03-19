package homedoctor.medicine.api.dto.alarm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder @Getter
public class ReceiverAlarmRequest {

    private Long user;

    public ReceiverAlarmRequest() {
    }

    public ReceiverAlarmRequest(Long user) {
        this.user = user;
    }
}
