package homedoctor.medicine.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.awt.*;
import java.sql.Blob;

@Entity
@Getter @Setter
@RequiredArgsConstructor
public class    PrescriptionImage extends DateTimeEntity {

    @Id @GeneratedValue
    @Column(name = "image_id")
    private Long id;

    // cascade = CascadeType.ALL 확인하기.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alarm_id", nullable = false)
    private Alarm alarm;

    @Column(name = "image", nullable = false, columnDefinition = "TEXT")
    private String image;


    @Builder
    public PrescriptionImage(Alarm alarm, String image) {
        this.alarm = alarm;
        this.image = image;
    }
}
