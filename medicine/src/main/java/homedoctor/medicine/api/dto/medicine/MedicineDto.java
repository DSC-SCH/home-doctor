package homedoctor.medicine.api.dto.medicine;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor @Getter
@Builder
public class MedicineDto {

    private Long medicineId;
    private String name;
    private String effect;
    private String saveMethod;
    private String validDate;
    private String dosage;
    private String badEffect;
}
