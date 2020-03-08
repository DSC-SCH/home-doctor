package homedoctor.medicine.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class ManagerUser extends DateTimeEntity {


    @Id @GeneratedValue
    @Column(name = "manager_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "manager_user")
    private User managerUser;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "reciever_user")
    private ReceiverUser receiverUser;

    // == 연관관계 메서드 == //


}
