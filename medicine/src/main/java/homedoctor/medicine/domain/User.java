package homedoctor.medicine.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Getter
@RequiredArgsConstructor
public class User extends DateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "user_status")
    private UserStatus userStatus;

    @Column (length = 15, nullable = false)
    private String phoneNum;

    private String token;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<Alarm> alarmList = new ArrayList<>();

    public void setToken(String token) {
        this.token = token;
    }

    // user 탈퇴 메소드
    public void deactivateUser() {
        this.userStatus = UserStatus.DEACTIVATE;
        this.snsId = "";
    }

    @Builder
    public User(String username, String birthday, String email, String snsId, SnsType snsType, GenderType genderType,
                UserStatus userStatus, String phoneNum, String token) {
        this.username = username;
        this.birthday = birthday;
        this.email = email;
        this.snsId = snsId;
        this.snsType = snsType;
        this.genderType = genderType;
        this.userStatus = userStatus;
        this.phoneNum = phoneNum;
        this.token = token;
    }
}
