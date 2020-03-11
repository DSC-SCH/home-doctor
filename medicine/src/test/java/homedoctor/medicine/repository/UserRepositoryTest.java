package homedoctor.medicine.repository;

import homedoctor.medicine.domain.GenderType;
import homedoctor.medicine.domain.SnsType;
import homedoctor.medicine.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserRepositoryTest {


    @Autowired
    EntityManager em;

    @Autowired
    UserRepository userRepository;

    @Test
    public void testCreateToken() throws Exception {
        // given
        User user = User.builder()
                .username("hee")
                .genderType(GenderType.WOMEN)
                .birthday("test")
                .email("test2")
                .snsType(SnsType.GOOGLE)
                .phoneNum("test")
                .build();

        // when
        userRepository.save(user);
        User findUser = userRepository.findOneById(user.getId());
        String findToken = userRepository.findUserToken(findUser);

        // then
        System.out.println("=========userToken=======");
        System.out.println(findUser.getToken());
        assertEquals(findToken, findUser.getToken());
    }

}