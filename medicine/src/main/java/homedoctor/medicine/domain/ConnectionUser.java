package homedoctor.medicine.domain;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class ConnectionUser extends DateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "connection_user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "manager_user")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "care_user")
    private User careUser;

    @Builder
    public ConnectionUser(User user, User careUser) {
        this.user = user;
        this.careUser = careUser;
    }

    public static ConnectionUser createConnection(User manager , User receiver) {
        ConnectionUser connectionUser = new ConnectionUser();
        connectionUser.setUser(manager);
        connectionUser.setCareUser(receiver);

        return connectionUser;
    }


}
