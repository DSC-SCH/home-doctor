package homedoctor.medicine.service;

import homedoctor.medicine.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static homedoctor.medicine.domain.GenderType.MEN;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    UserService userService;

    @Test
    public void userTest() throws Exception {
        // given
        User user = User.builder()
                .username("nathan")
                .genderType(MEN)
                .build();

        // when
        Long userID = userService.join(user);

        // then
        User findUser = userService.findOne(userID);
        assertEquals(user, findUser);
    }


}
