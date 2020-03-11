package homedoctor.medicine.service;

import homedoctor.medicine.domain.Alarm;
import homedoctor.medicine.domain.GenderType;
import homedoctor.medicine.domain.PrescriptionImage;
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
public class ImageServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    AlarmService alarmService;

    @Autowired
    PrescriptionImageService imageService;

    @Test
    public void saveImageTest() throws Exception {
        // given
        Alarm alarm = Alarm.builder()
                .title("nathan")
                .build();

        PrescriptionImage image = PrescriptionImage.builder()
                .build();

        // when
//        Long saveId = imageService.save(alarm, image);

        // then
//        assertEquals(image.getId(), saveId);
    }

    @Test
    public void findAllByUserTest() throws Exception {
        // given
        Alarm alarm = Alarm.builder()
                .title("nathan")
                .build();

        PrescriptionImage image1 = PrescriptionImage.builder().build();
        PrescriptionImage image2 = PrescriptionImage.builder().build();

        // when
//        Long imageId1 = imageService.save(alarm, image1);
//        Long imageId2 = imageService.save(alarm, image2);
//
//        // then
//        assertEquals(imageService.findImagesByUser(alarm).get(0), image1);
//        assertEquals(imageService.findImagesByUser(alarm).get(1), image2);
    }
}
