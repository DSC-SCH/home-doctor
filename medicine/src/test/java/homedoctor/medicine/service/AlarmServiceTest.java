package homedoctor.medicine.service;

import homedoctor.medicine.domain.Alarm;
import homedoctor.medicine.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AlarmServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    AlarmService alarmService;

    @Autowired
    UserService userService;

    @Test
    public void saveTest() throws Exception {
        // given
        User user = User.builder()
                .username("nathan")
                .build();

        Alarm alarm = Alarm.builder()
                .title("nathan")
                .build();

        // when

        // then
    }

    @Test
    public void findAlarmTest() throws Exception {
        // given
        User user = User.builder()
                .username("natha")
                .build();

        Alarm alarm = Alarm.builder()
                .title("nathan")
                .build();

        // when


        // then
    }

    @Test
    public void findAlarmByUserTest() throws Exception {
        // given
        User user1 = User.builder()
                .username("nathan")
                .build();

        Alarm alarm1 = Alarm.builder()
                .title("test1")
                .build();

        Alarm alarm2 = Alarm.builder()
                .title("test2")
                .build();

        // when
//        Long userId = userService.save(user1);

        // then
    }
}
