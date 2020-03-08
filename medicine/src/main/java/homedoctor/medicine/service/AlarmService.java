package homedoctor.medicine.service;

import homedoctor.medicine.domain.Alarm;
import homedoctor.medicine.domain.User;
import homedoctor.medicine.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmService {

    private final AlarmRepository alarmRepository;

    @Transactional
    public Long save(Alarm alarm) {
        alarmRepository.save(alarm);
        return alarm.getId();
    }

    public List<Alarm> findAlarmsByUser(User user) {
        return alarmRepository.findAllByUser(user);
    }

    public Alarm findAlarm(Long alarmId) {
        return alarmRepository.findOne(alarmId);
    }

    @Transactional
    public void delete(Alarm alarm) {
        alarmRepository.delete(alarm);
    }
}
