package homedoctor.medicine.service;

import homedoctor.medicine.domain.ConnectCode;
import homedoctor.medicine.domain.GenderType;
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
public class ConnectServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    ConnectService connectService;

    @Autowired
    UserService userService;

    @Test
    public void createCodeTest() throws Exception {
        // given
        User giver = User.builder()
                .username("nathan")
                .genderType(GenderType.MEN)
                .build();

        User receiver = User.builder()
                .username("koo")
                .genderType(GenderType.WOMEN)
                .build();

        ConnectCode connectCode = ConnectCode.builder()
                .code("1234")
                .build();

        // when
        Long giverId = userService.join(giver);
        Long receiverId = userService.join(receiver);
        Long connectCodeId = connectService.saveCode(giver, connectCode);


        // then
        assertEquals(connectCode.getCode(), "1234");
    }

    @Test
    public void connectTest() throws Exception {
        // given
        User giver = User.builder()
                .username("nathan")
                .build();

        User receiver = User.builder()
                .username("koo")
                .build();

        ConnectCode connectCode = ConnectCode.builder()
                .code("1234")
                .build();

        // when
        Long giverId = userService.join(giver);
        Long receiverId = userService.join(receiver);
        Long connectCodeId = connectService.saveCode(giver, connectCode);

        // then
        assertEquals(connectService.findCode(connectCodeId).getCode(), "1234");
    }
}
