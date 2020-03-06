package homedoctor.medicine.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter @Setter
@Builder
public class User {

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

    @Temporal(TemporalType.TIMESTAMP)
    private Date joinDate;

    private int snsType;

    @Enumerated(EnumType.STRING)
    private GenderType genderType;

    @Column (length = 15)
    private String phoneNum;

}
