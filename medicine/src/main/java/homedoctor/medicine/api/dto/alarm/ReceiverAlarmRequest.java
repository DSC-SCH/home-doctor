package homedoctor.medicine.api.dto.alarm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Builder @Getter
public class ReceiverAlarmRequest {

    private Long user;
}
