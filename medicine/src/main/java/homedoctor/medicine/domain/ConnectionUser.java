package homedoctor.medicine.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConnectionUser {

    @Id @GeneratedValue
    @Column(name = "care_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "careUser_id")
    private User careUser;

    @Temporal(TemporalType.DATE)
    private Date create_at;


    public static ConnectionUser createConnectionUser(User user, User careUser) {
        ConnectionUser connectionUser = new ConnectionUser();
        connectionUser.setUser(user);
        connectionUser.setCareUser(careUser);
        return connectionUser;
    }
}
