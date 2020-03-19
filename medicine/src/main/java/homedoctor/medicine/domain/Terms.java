package homedoctor.medicine.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Getter
@RequiredArgsConstructor
public class Terms {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Builder
    public Terms(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
