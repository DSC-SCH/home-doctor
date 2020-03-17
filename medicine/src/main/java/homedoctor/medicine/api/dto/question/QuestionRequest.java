package homedoctor.medicine.api.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestionRequest {

    private String email;

    private String content;


    public final boolean validProperties() {
        if (email != null && content != null) {
            return true;
        }
        return false;
    }
}
