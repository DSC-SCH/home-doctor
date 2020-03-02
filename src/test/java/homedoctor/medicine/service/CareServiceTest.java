package homedoctor.medicine.service;

import homedoctor.medicine.domain.Care;
import homedoctor.medicine.domain.GenderType;
import homedoctor.medicine.domain.User;
import org.graalvm.compiler.lir.LIRInstruction;
import org.graalvm.compiler.word.Word;
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
public class CareServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    UserService userService;

    @Autowired
    CareService careService;

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

        Care care = Care.builder()
                .build();

        // when
        Long careId = careService.save(user1, user2, care);

        // then
        assertEquals(careId, care.getId());
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

        Care care = Care.builder()
                .build();

        // when
        Long userId1 = userService.join(user1);
        Long userId2 = userService.join(user2);
        Long userId3 = userService.join(user3);
        Long careId = careService.save(user3, user1, care);


        Care findCare = careService.findOne(careId);
        findCare.addCareUser(user2);

        // then
        assertEquals(care.getCareUser().get(0), user1);
        assertEquals(care.getCareUser().get(1), user2);
    }

}
