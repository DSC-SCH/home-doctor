package homedoctor.medicine.api.dto.alarm;

import lombok.Data;

@Data
public class CreateAlarmResponse {

    private Long id;

    public CreateAlarmResponse(Long id) {
        this.id = id;
    }
}
