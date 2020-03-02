package homedoctor.medicine.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Builder
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(length = 20)
    private String username;

    @Column(length = 10)
    private String birthday;

    private String email;

    private String kakaoId;

    private String googleId;

    private String facebookId;

    private String naverId;

    @Temporal(TemporalType.DATE)
    private Date joinDate;

    private int snsType;

    private GenderType genderType;

    @Column (length = 15)
    private String phoneNum;
}
