package homedoctor.medicine.api.dto.alarm;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class DeleteAlarmResponse {

    private Long id;

    private String title;


    public DeleteAlarmResponse() {
    }

    public DeleteAlarmResponse(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
