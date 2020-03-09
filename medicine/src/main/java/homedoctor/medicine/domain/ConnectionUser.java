package homedoctor.medicine.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class ConnectionUser extends DateTimeEntity {

    @Id @GeneratedValue
    @Column(name = "connection_user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "care_user", nullable = false)
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
