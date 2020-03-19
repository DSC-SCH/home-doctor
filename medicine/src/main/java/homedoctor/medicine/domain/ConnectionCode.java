package homedoctor.medicine.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@RequiredArgsConstructor
public class ConnectionCode extends DateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "code_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "create_user", nullable = false)
    private User user;

    @Column(length = 45, nullable = false)
    private String code;

    @Column(nullable = false)
    private Date life;

    // == 연관관계 메서드 == //
    public void setUser(User user) {
        this.user = user;
    }

    @Builder
    public ConnectionCode(User user, String code, Date life) {
        this.user = user;
        this.code = code;
        this.life = life;
    }
}
