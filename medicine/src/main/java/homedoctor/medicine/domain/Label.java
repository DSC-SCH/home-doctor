package homedoctor.medicine.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Label {

    @Id @GeneratedValue
    @Column(name = "label_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "label_title", length = 10)
    private String title;

    @Column(name = "label_color", length = 7)
    private String color;

    //== 연관관계 메서드 ==//
    public void setUser(User user) {
        this.user = user;
    }
}
