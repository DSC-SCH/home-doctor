package homedoctor.medicine.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@RequiredArgsConstructor
public class ConnectionCode extends DateTimeEntity {

    @Id @GeneratedValue
    @Column(name = "code_id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
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
