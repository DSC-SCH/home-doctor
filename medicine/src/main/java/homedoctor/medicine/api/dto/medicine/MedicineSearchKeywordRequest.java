package homedoctor.medicine.api.dto.medicine;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data @Getter
public class MedicineSearchKeywordRequest {

    private String keyword;

    public MedicineSearchKeywordRequest() {
    }

    public MedicineSearchKeywordRequest(String keyword) {
        this.keyword = keyword;
    }
}
