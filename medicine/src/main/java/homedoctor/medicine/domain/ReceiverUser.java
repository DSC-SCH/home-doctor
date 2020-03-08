package homedoctor.medicine.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class ReceiverUser extends DateTimeEntity {

    @Id @GeneratedValue
    @Column(name = "receiver_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_user")
    private User receiverUser;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_user")
    private ManagerUser managerUser;

    // == 연관관계 메서드 == //

}
