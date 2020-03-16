package homedoctor.medicine.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class AlarmCount {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private User userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "alarm_id")
    private Alarm alarmId;

    // Date 값을 잘라서 년:월:일 로 저장. (2020:11:20)
    private String alarmDate;

    private Integer counts;

    @Builder
    public AlarmCount(User user, Alarm alarm, String alarmDate, Integer count) {
        this.userId = user;
        this.alarmId = alarm;
        this.alarmDate = alarmDate;
        this.counts = count;
    }

    // == 엔티티 메소드 == //
    public void updateCounts(Integer count) {
        this.counts = count;
    }

}
