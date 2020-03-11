package homedoctor.medicine;

import homedoctor.medicine.domain.*;
import homedoctor.medicine.repository.AlarmRepository;
import homedoctor.medicine.repository.LabelRepository;
import homedoctor.medicine.repository.UserRepository;
import homedoctor.medicine.service.LabelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit();
    }


    private final EntityManager em;

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;
        private final UserRepository userRepository;

        private final LabelRepository labelRepository;
        private final AlarmRepository alarmRepository;

        private final LabelService labelService;

        public void dbInit() {
            User nathan = User.builder()
                    .username("nathan")
                    .genderType(GenderType.MEN)
                    .birthday("1996-02-20")
                    .email("skeks463@gmail.com")
                    .snsType(SnsType.GOOGLE)
                    .phoneNum("010-6666-1111")
                    .snsId("asdasdasd")
                    .build();

            User seongA = User.builder()
                    .username("seongA")
                    .genderType(GenderType.WOMEN)
                    .birthday("1998-02-20")
                    .email("seongA@gmail.com")
                    .snsType(SnsType.KAKAO)
                    .phoneNum("010-6666-1111")
                    .snsId("asdasdasasdd")
                    .build();

            User Park = User.builder()
                    .username("Park")
                    .genderType(GenderType.MEN)
                    .birthday("2000-01-01")
                    .email("park@naver.com")
                    .snsType(SnsType.KAKAO)
                    .phoneNum("010-2341-2312")
                    .snsId("seasdasdasd")
                    .build();

            //User Save
            userRepository.save(nathan);
            userRepository.save(seongA);
            userRepository.save(Park);

            Label nathanLabel1 = Label.builder()
                    .user(nathan)
                    .title("감기")
                    .color("12312")
                    .build();

            Label nathanLabel2 = Label.builder()
                    .user(nathan)
                    .title("장염")
                    .color("12322")
                    .build();

            Label nathanLabel3 = Label.builder()
                    .user(nathan)
                    .title("변비")
                    .color("12312")
                    .build();

            Label ALabel1 = Label.builder()
                    .user(seongA)
                    .title("감기")
                    .color("12312")
                    .build();

            Label ALabel2 = Label.builder()
                    .user(seongA)
                    .title("장염")
                    .color("12312")
                    .build();

            Label ALabel3 = Label.builder()
                    .user(seongA)
                    .title("변비")
                    .color("12312")
                    .build();

            Label parkLabel1 = Label.builder()
                    .user(Park)
                    .title("감기")
                    .color("12312")
                    .build();

            Label parkLabel2 = Label.builder()
                    .user(Park)
                    .title("장염")
                    .color("1212")
                    .build();

            Label parkLabel3 = Label.builder()
                    .user(Park)
                    .title("변비")
                    .color("12312")
                    .build();

            // Label Save
            labelService.save(nathanLabel1);
            labelService.save(nathanLabel2);
            labelService.save(nathanLabel3);
            labelService.save(ALabel1);
            labelService.save(ALabel2);
            labelService.save(ALabel3);
            labelService.save(parkLabel1);
            labelService.save(parkLabel2);
            labelService.save(parkLabel3);

            Date date = new Date();

            Alarm nathanAlarm1 = Alarm.builder()
                    .user(nathan)
                    .title("test")
                    .label(nathanLabel1)
                    .startDate(date)
                    .endDate(date)
                    .times("09:00/12:00/13:00")
                    .repeats("3")
                    .alarmStatus(AlarmStatus.ENABLE)
                    .build();

            Alarm nathanAlarm2 = Alarm.builder()
                    .user(nathan)
                    .title("test")
                    .label(nathanLabel2)
                    .startDate(date)
                    .endDate(date)
                    .times("09:00/12:00/13:00")
                    .repeats("3")
                    .alarmStatus(AlarmStatus.ENABLE)
                    .build();

            Alarm seongAlarm1 = Alarm.builder()
                    .user(seongA)
                    .title("test")
                    .label(ALabel1)
                    .startDate(date)
                    .endDate(date)
                    .times("09:00/12:00/13:00")
                    .repeats("3")
                    .alarmStatus(AlarmStatus.ENABLE)
                    .build();

            Alarm seongAlarm2 = Alarm.builder()
                    .user(nathan)
                    .title("test")
                    .label(ALabel2)
                    .startDate(date)
                    .endDate(date)
                    .times("09:00/12:00/13:00")
                    .repeats("3")
                    .alarmStatus(AlarmStatus.ENABLE)
                    .build();

            Alarm parkAlarm = Alarm.builder()
                    .user(nathan)
                    .title("test")
                    .label(parkLabel1)
                    .startDate(date)
                    .endDate(date)
                    .times("09:00/12:00/13:00")
                    .repeats("3")
                    .alarmStatus(AlarmStatus.ENABLE)
                    .build();

            alarmRepository.save(nathanAlarm1);
            alarmRepository.save(nathanAlarm2);
            alarmRepository.save(seongAlarm1);
            alarmRepository.save(seongAlarm1);
            alarmRepository.save(parkAlarm);
        }


    }

}
