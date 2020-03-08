package homedoctor.medicine.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter @Setter
public abstract class DateTimeEntity {

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;
}
