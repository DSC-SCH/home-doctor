package homedoctor.medicine.api.controller;

import homedoctor.medicine.api.dto.alarm.AlarmDto;
import homedoctor.medicine.domain.Alarm;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AlarmApiController {

    private final AlarmService alarmService;

    @GetMapping("/alarm/{alarm_id}")
    public Alarm findOneAlarm(
            @PathVariable("alarm_id") Long id) {
        return alarmService.findAlarm(id);
    }

    @GetMapping("/alarm")
    public List<Alarm> getAllAlarmByUser(User user) {
        List<Alarm> findAllAlarm = alarmService.findAlarmsByUser(user);

        // 노출되는 알람 입력 정보중 필요한 필드만 가져오기.(엔티티 노출하지 않기!!)

        return findAllAlarm;
    }
}
