package homedoctor.medicine.domain;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class Medicine {

    @Id @GeneratedValue
    @Column(name = "medicine_id")
    private Long id;

    @Column(name = "item_code")
    private Long code;

    @Column(name = "medicine_name", length = 50)
    private String name;

    @Column(name = "effect", columnDefinition = "TEXT")
    private String effect;

    @Column(name = "save_method")
    private String saveMethod;

    private String validDate;

    private String dosage;

    @Column(columnDefinition = "TEXT")
    private String badEffect;

    @Column(columnDefinition = "TEXT")
    private String precautions;

    @Column(columnDefinition = "TEXT")
    private String combinationBan;

}
