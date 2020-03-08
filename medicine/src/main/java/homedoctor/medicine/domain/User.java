package homedoctor.medicine.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@RequiredArgsConstructor
public class User extends DateTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String password;

    @Column(length = 20)
    private String username;

    @Column(length = 10)
    private String birthday;

    private String email;

    private String kakaoId;

    private String googleId;

    private String facebookId;

    private String naverId;

    private int snsType;

    @Enumerated(EnumType.STRING)
    private GenderType genderType;

    @Column (length = 15)
    private String phoneNum;

    @OneToMany(mappedBy = "user")
    private List<Alarm> alarmList = new ArrayList<>();

    // 쿼리 날리기
//    @OneToMany(mappedBy = "careUser", cascade = CascadeType.ALL)
//    private List<ConnectionUser> receiverUserList = new ArrayList<>();
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<ConnectionUser> managerUserList = new ArrayList<>();

    @Builder
    public User(String password, String username, String birthday, String email, String kakaoId, String googleId, String facebookId, String naverId, int snsType, GenderType genderType, String phoneNum, List<Alarm> alarmList) {
        this.password = password;
        this.username = username;
        this.birthday = birthday;
        this.email = email;
        this.kakaoId = kakaoId;
        this.googleId = googleId;
        this.facebookId = facebookId;
        this.naverId = naverId;
        this.snsType = snsType;
        this.genderType = genderType;
        this.phoneNum = phoneNum;
        this.alarmList = alarmList;
    }
}
