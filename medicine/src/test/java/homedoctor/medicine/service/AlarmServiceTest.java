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
                .content("test")
                .build();

        // when
        Long saveId = alarmService.save(alarm);

        // then
        assertThat(alarm.getTitle()).isEqualTo("nathan");
        assertThat(alarm.getContent()).isEqualTo("test");
    }

    @Test
    public void findAlarmTest() throws Exception {
        // given
        User user = User.builder()
                .username("natha")
                .build();

        Alarm alarm = Alarm.builder()
                .title("nathan")
                .content("Updatetest")
                .build();

        // when
        Long saveId = alarmService.save(alarm);


        // then
        assertThat(alarmService.findAlarm(saveId)).isEqualTo(alarm);
    }

    @Test
    public void findAlarmByUserTest() throws Exception {
        // given
        User user1 = User.builder()
                .username("nathan")
                .build();

        Alarm alarm1 = Alarm.builder()
                .title("test1")
                .content("contentTest2")
                .build();

        Alarm alarm2 = Alarm.builder()
                .title("test2")
                .content("contentTest2")
                .build();

        // when
        Long userId = userService.join(user1);
        Long saveId1 = alarmService.save(alarm1);
        Long saveId2 = alarmService.save(alarm2);

        // then
        assertThat(alarmService.findAlarmsByUser(user1).get(0).getTitle()).isEqualTo("test1");
        assertThat(alarmService.findAlarmsByUser(user1).get(1).getTitle()).isEqualTo("test2");
    }
}
