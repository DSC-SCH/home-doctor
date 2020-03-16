package homedoctor.medicine.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class User extends DateTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(length = 20, name = "user_name", nullable = false)
    private String username;

    @Column(length = 10, nullable = false)
    private String birthday;

    @Column(nullable = false)
    private String email;

    @Column(length = 4095)
    private String snsId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SnsType snsType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GenderType genderType;

    @Column (length = 15, nullable = false)
    private String phoneNum;

    private String token;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<Alarm> alarmList = new ArrayList<>();

    public void setToken(String token) {
        this.token = token;
    }

    @Builder
    public User(String username, String birthday, String email, String snsId, SnsType snsType, GenderType genderType, String phoneNum, String token) {
        this.username = username;
        this.birthday = birthday;
        this.email = email;
        this.snsId = snsId;
        this.snsType = snsType;
        this.genderType = genderType;
        this.phoneNum = phoneNum;
        this.token = token;
    }
}
