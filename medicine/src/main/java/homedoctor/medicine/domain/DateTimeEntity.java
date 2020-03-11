package homedoctor.medicine.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.Date;

@MappedSuperclass
@Getter @Setter
public abstract class DateTimeEntity {

    private Date createdDate = new Date();

    private Date lastModifiedDate = new Date();
}
