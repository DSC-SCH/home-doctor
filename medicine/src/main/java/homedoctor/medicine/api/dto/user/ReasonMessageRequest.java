package homedoctor.medicine.api.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ReasonMessageRequest {

    private String content;

    public ReasonMessageRequest() {
    }

    public ReasonMessageRequest(String content) {
        this.content = content;
    }
}
