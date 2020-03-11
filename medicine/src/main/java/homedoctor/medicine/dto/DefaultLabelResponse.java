package homedoctor.medicine.dto;

import homedoctor.medicine.domain.Label;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class DefaultLabelResponse {

    private int status;

    private String responseMessage;

    private Label label;

    private List<Label> labelList;
}
