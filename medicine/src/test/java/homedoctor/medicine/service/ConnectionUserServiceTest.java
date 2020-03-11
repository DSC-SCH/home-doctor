package homedoctor.medicine.service;

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
                .birthday("test")
                .email("test")
                .snsType(SnsType.GOOGLE)
                .phoneNum("test")
                .build();

        User user2 = User.builder()
                .username("koo")
                .genderType(GenderType.WOMEN)
                .birthday("test")
                .email("test1")
                .snsType(SnsType.GOOGLE)
                .phoneNum("test")
                .build();


        // when
//        Long careId = connectionUserService.save(user1, user2);

        // then

    }

    @Test
    public void findCareUsersTest() throws Exception {
        // given
        User user1 = User.builder()
                .username("nathan")
                .genderType(GenderType.MEN)
                .birthday("test")
                .email("test")
                .snsType(SnsType.GOOGLE)
                .phoneNum("test")
                .build();

        User user2 = User.builder()
                .username("koo")
                .genderType(GenderType.WOMEN)
                .birthday("test")
                .email("test1")
                .snsType(SnsType.GOOGLE)
                .phoneNum("test")
                .build();

        User user3 = User.builder()
                .username("hee")
                .genderType(GenderType.WOMEN)
                .birthday("test")
                .email("test2")
                .snsType(SnsType.GOOGLE)
                .phoneNum("test")
                .build();


        // when

//        Long userId1 = userService.save(user1);
//        Long userId2 = userService.save(user2);
//        Long userId3 = userService.save(user3);
//        Long careId1 = connectionUserService.save(user3, user1);
//        Long careId2 = connectionUserService.save(user3, user2);
//        List<User> receiverUsers = connectionUserService.findAllReceiverByUser(user3);


        // then
//        assertEquals(receiverUsers.get(0).getId(), user1.getId());
//        assertEquals(receiverUsers.get(1).getId(), user2.getId());
    }

    @Test
    public void TestFindManagerUser() throws Exception {
        // given
        User user1 = User.builder()
                .username("nathan")
                .genderType(GenderType.MEN)
                .birthday("test")
                .email("test")
                .snsType(SnsType.GOOGLE)
                .phoneNum("test")
                .build();

        User user2 = User.builder()
                .username("koo")
                .genderType(GenderType.WOMEN)
                .birthday("test")
                .email("test1")
                .snsType(SnsType.GOOGLE)
                .phoneNum("test")
                .build();

        User user3 = User.builder()
                .username("hee")
                .genderType(GenderType.WOMEN)
                .birthday("test")
                .email("test2")
                .snsType(SnsType.GOOGLE)
                .phoneNum("test")
                .build();

        // when
//        Long userId1 = userService.save(user1);
//        Long userId2 = userService.save(user2);
//        Long userId3 = userService.save(user3);
//        Long careId1 = connectionUserService.save(user3, user1);
//        Long careId2 = connectionUserService.save(user3, user2);
//        Long careId3 = connectionUserService.save(user1, user2);
//        List<User> managerUserList = connectionUserService.findALlManagerByUser(user2);


        // then
//
//        assertEquals(managerUserList.get(0).getId(), userId3);
//        assertEquals(managerUserList.get(1).getId(), userId1);
    }

}
