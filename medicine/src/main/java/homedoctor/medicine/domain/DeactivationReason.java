package homedoctor.medicine.domain;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Getter
public class DeactivationReason {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "reason_id")
    private Long id;

    @Column(name = "content")
    private String content;

    public DeactivationReason() {
    }

    @Builder
    public DeactivationReason(Long id, String content) {
        this.id = id;
        this.content = content;
    }
}
