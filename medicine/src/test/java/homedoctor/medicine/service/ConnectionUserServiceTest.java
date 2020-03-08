package homedoctor.medicine.service;

import homedoctor.medicine.domain.GenderType;
import homedoctor.medicine.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ConnectionUserServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    UserService userService;

    @Autowired
    ConnectionUserService connectionUserService;

    @Test
    public void saveCareTest() throws Exception {
        // given
        User user1 = User.builder()
                .username("nathan")
                .genderType(GenderType.MEN)
                .build();

        User user2 = User.builder()
                .username("koo")
                .genderType(GenderType.WOMEN)
                .build();


        // when
        Long careId = connectionUserService.save(user1, user2);

        // then

    }

    @Test
    public void findCareUsersTest() throws Exception {
        // given
        User user1 = User.builder()
                .username("nathan")
                .genderType(GenderType.MEN)
                .build();

        User user2 = User.builder()
                .username("koo")
                .genderType(GenderType.WOMEN)
                .build();

        User user3 = User.builder()
                .username("hee")
                .genderType(GenderType.WOMEN)
                .build();


        // when
        Long userId1 = userService.join(user1);
        Long userId2 = userService.join(user2);
        Long userId3 = userService.join(user3);
        System.out.println("=================");
        System.out.println("userId1 = " + userId1);
        System.out.println("userId2 = " + userId2);
        System.out.println("userId3 = " + userId3);
        Long careId1 = connectionUserService.save(user3, user1);
        Long careId2 = connectionUserService.save(user3, user2);
        List<User> receiverUsers = connectionUserService.findAllReceiverByUser(user3);

        // then
        assertEquals(receiverUsers.get(0), user1);
        assertEquals(receiverUsers.get(1), user2);
    }

}
