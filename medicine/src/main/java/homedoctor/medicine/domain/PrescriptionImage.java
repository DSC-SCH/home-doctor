package homedoctor.medicine.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.sql.Blob;
import java.sql.Date;

@Entity
@Getter
@Builder
public class PrescriptionImage {

    @Id @GeneratedValue
    @Column(name = "image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "image")
    private Blob image;

    @Column(name = "image_created_at")
    private Date created_at;


    // == 연관관계 메서드 == //
    public void setUser(User user) {
        this.user = user;
    }
}
