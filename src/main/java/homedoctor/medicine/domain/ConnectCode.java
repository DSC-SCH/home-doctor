package homedoctor.medicine.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Builder
public class ConnectCode {

    @Id @GeneratedValue
    @Column(name = "connect_id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 45)
    private String code;

    private int life;

    // == 연관관계 메서드 == //
    public void setUser(User user) {
        this.user = user;
    }
}
