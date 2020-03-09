package homedoctor.medicine.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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

    private String kakaoId;

    private String googleId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SnsType snsType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GenderType genderType;

    @Column (length = 15, nullable = false)
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
    public User(String username, String birthday, String email, String kakaoId, String googleId, SnsType snsType, GenderType genderType, String phoneNum, List<Alarm> alarmList) {
        this.username = username;
        this.birthday = birthday;
        this.email = email;
        this.kakaoId = kakaoId;
        this.googleId = googleId;
        this.snsType = snsType;
        this.genderType = genderType;
        this.phoneNum = phoneNum;
        this.alarmList = alarmList;
    }
}
