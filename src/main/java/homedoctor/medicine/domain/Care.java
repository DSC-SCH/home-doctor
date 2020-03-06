package homedoctor.medicine.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Builder
public class Care {

    @Id @GeneratedValue
    @Column(name = "care_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, targetEntity = User.class)
    private List<User> careUser;

    @Temporal(TemporalType.DATE)
    private Date create_at;

    // == 연관관계 메서드 == //
    public void setUser(User user) {
        this.user = user;
    }

    // == 앱 로직 == //
    public void setCareUser(User user) {
        List<User> users = new ArrayList<>();
        users.add(user);
        this.careUser = users;
    }

    public void addCareUser(User user) {
        this.careUser.add(user);
    }
}
