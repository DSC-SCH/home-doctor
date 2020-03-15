package homedoctor.medicine.api.dto.receiver;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter @Setter
public class CreateReceiverAlarmResponse {

    private Long alarmId;

    public CreateReceiverAlarmResponse() {
    }

    @Builder
    public CreateReceiverAlarmResponse(Long alarmId) {
        this.alarmId = alarmId;
    }
}
