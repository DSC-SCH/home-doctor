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
    @Column(name = "care_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "careUser_id")
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
