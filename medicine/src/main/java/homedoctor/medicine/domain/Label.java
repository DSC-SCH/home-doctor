package homedoctor.medicine.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter @Setter
@RequiredArgsConstructor
public class Label extends DateTimeEntity {

    @Id @GeneratedValue
    @Column(name = "label_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "label_user", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(name = "label_title", length = 10, nullable = false)
    private String title;

    @Column(name = "label_color", length = 7, nullable = false)
    private String color;

    //== 연관관계 메서드 ==//
    public void setUser(User user) {
        this.user = user;
    }

    @Builder
    public Label(User user, String title, String color) {
        this.user = user;
        this.title = title;
        this.color = color;
    }
}
