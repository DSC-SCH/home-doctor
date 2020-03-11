package homedoctor.medicine.service;

import homedoctor.medicine.domain.GenderType;
import homedoctor.medicine.domain.Label;
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
public class LabelServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    LabelService labelService;

    @Autowired
    UserService userService;

    @Test
    public void saveLabelTest() throws Exception {
        // given
        User user = User.builder()
                .username("nathan")
                .phoneNum("01012341234").build();

        Label label = Label.builder()
                .color("red")
                .title("감기")
                .build();

        // when
//        Long saveId = labelService.saveLabel(user, label);

        // then
//        assertEquals(label.getId(), saveId);
    }

    @Test
    public void findByUserTest() throws Exception {
        // given
        User user = User.builder()
                .username("nathan")
                .genderType(GenderType.MEN)
                .build();

        Label label1 = Label.builder()
                .title("감기")
                .color("red")
                .build();

        Label label2 = Label.builder()
                .title("변비")
                .color("blue")
                .build();

        // when
//        Long userId = userService.save(user);
//        Long saveId1 = labelService.saveLabel(user, label1);
//        Long saveId2 = labelService.saveLabel(user, label2);
//
//        // then
//        assertEquals(labelService.fineLabelsByUser(user).get(0), label1);
//        assertEquals(labelService.fineLabelsByUser(user).get(1), label2);
    }
}
