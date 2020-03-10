package homedoctor.medicine.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Blob;

@Entity
@Getter @Setter
@RequiredArgsConstructor
public class PrescriptionImage extends DateTimeEntity {

    @Id @GeneratedValue
    @Column(name = "image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alarm_id", nullable = false)
    private Alarm alarm;

    @Column(name = "image", nullable = false)
    private Blob image;

    @Builder
    public PrescriptionImage(Alarm alarm, Blob image) {
        this.alarm = alarm;
        this.image = image;
    }
}
