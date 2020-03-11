package homedoctor.medicine.service;

import homedoctor.medicine.domain.ConnectionCode;
import homedoctor.medicine.domain.GenderType;
import homedoctor.medicine.domain.SnsType;
import homedoctor.medicine.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ConnectionCodeServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    ConnectionCodeService connectionCodeService;

    @Autowired
    UserService userService;

    @Test
    public void createCodeTest() throws Exception {
        // given
        User giver = User.builder()
                .username("nathan")
                .genderType(GenderType.MEN)
                .birthday("test")
                .email("test")
                .snsType(SnsType.GOOGLE)
                .phoneNum("test")
                .build();

        User receiver = User.builder()
                .username("koo")
                .genderType(GenderType.WOMEN)
                .birthday("test")
                .email("test1")
                .snsType(SnsType.GOOGLE)
                .phoneNum("test")
                .build();

        ConnectionCode connectionCode = ConnectionCode.builder()
                .code("1234")
                .build();

        // when
//        Long giverId = userService.save(giver);
//        Long receiverId = userService.save(receiver);
//        Long connectCodeId = connectionCodeService.saveCode(giver, connectionCode);


        // then
        assertEquals(connectionCode.getCode(), "1234");
    }

    @Test
    public void connectTest() throws Exception {
        // given
        User giver = User.builder()
                .username("nathan")
                .genderType(GenderType.MEN)
                .birthday("test")
                .email("test")
                .snsType(SnsType.GOOGLE)
                .phoneNum("test")
                .build();

        User receiver = User.builder()
                .username("koo")
                .genderType(GenderType.WOMEN)
                .birthday("test")
                .email("test1")
                .snsType(SnsType.GOOGLE)
                .phoneNum("test")
                .build();

        ConnectionCode connectionCode = ConnectionCode.builder()
                .code("1234")
                .build();


        // when
//        Long giverId = userService.save(giver);
//        Long receiverId = userService.save(receiver);
//        Long connectCodeId = connectionCodeService.saveCode(giver, connectionCode);

        // then
//        assertEquals(connectionCodeService.findCode(connectCodeId).getConnectionCode(), "1234");
    }
}
