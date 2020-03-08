package homedoctor.medicine.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Blob;
import java.sql.Date;

@Entity
@Getter @Setter
@RequiredArgsConstructor
public class PrescriptionImage extends DateTimeEntity {

    @Id @GeneratedValue
    @Column(name = "image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alarm_id")
    private Alarm alarm;

    @Column(name = "image")
    private Blob image;

    @Builder
    public PrescriptionImage(Alarm alarm, Blob image) {
        this.alarm = alarm;
        this.image = image;
    }

}
