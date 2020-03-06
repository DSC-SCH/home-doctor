package homedoctor.medicine.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter @Setter
@Builder
public class Alarm {

    @Id @GeneratedValue
    @Column(name = "alarm_id")
    private Long id;

    @Column(name = "alarm_title", length = 64)
    private String title;

    @Column(name = "alarm_content", length = 512)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "label_id")
    private Label label;

    @Column(name = "alarm_start_df", length = 10)
    private Date startDt;

    @Column(name = "alarm_end_df", length = 10)
    private Date endDt;

    @Column(name = "alarm_times", length = 128)
    private String times;

    @Column(name = "alarm_repeats", length = 32)
    private String repeats;

    @Column(name = "alarm_enabled")
    private int enabled;


    //== 연관관계 메서드 ==//
//    public void creatAlarm(User user) {
//        this.user = user;
//        user.getAlarms().add(this);
//    }
}
