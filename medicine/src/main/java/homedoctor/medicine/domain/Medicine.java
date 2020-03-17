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

    @Column(name = "item_code", columnDefinition = "TEXT")
    private String code;

    @Column(name = "medicine_name", length = 50, columnDefinition = "TEXT")
    private String name;

    @Column(name = "effect", columnDefinition = "TEXT")
    private String effect;

    @Column(name = "save_method", columnDefinition = "TEXT")
    private String saveMethod;

    @Column(name = "valid_date", columnDefinition = "TEXT")
    private String validDate;

    @Column(columnDefinition = "TEXT")
    private String dosage;

    @Column(columnDefinition = "TEXT", name = "precaution")
    private String precaution;


}
