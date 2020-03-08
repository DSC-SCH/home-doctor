package homedoctor.medicine.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter @Setter
@RequiredArgsConstructor
public class Alarm extends DateTimeEntity{

    @Id @GeneratedValue
    @Column(name = "alarm_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "alarm_title", length = 64)
    private String title;

    @Column(name = "alarm_content", length = 512)
    private String content;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "label_id")
    private Label label;

    @Column(name = "alarm_start_df", length = 10)
    private Date startDate;

    @Column(name = "alarm_end_df", length = 10)
    private Date endDate;

    @Column(name = "alarm_times", length = 128)
    private String times;

    @Column(name = "alarm_repeats", length = 32)
    private String repeats;

    @Column(name = "alarm_enabled")
    private int enabled;

    @OneToMany(mappedBy = "alarm")
    private List<PrescriptionImage> prescriptionImageList = new ArrayList<>();

    //== 연관관계 메서드 ==//
//    public void creatAlarm(User user) {
//        this.user = user;
//        user.getAlarms().add(this);
//    }

    @Builder
    public Alarm(User user, String title, String content, Label label, Date startDate, Date endDate, String times, String repeats, int enabled, List<PrescriptionImage> prescriptionImageList) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.label = label;
        this.startDate = startDate;
        this.endDate = endDate;
        this.times = times;
        this.repeats = repeats;
        this.enabled = enabled;
        this.prescriptionImageList = prescriptionImageList;
    }
}
