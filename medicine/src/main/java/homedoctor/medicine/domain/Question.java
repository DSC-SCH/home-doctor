package homedoctor.medicine.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Getter
@Entity
@RequiredArgsConstructor
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "question_id")
    private Long id;

    @Column(name = "user_email")
    private String email;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_date")
    private Date createdDate;

    @Builder
    public Question(String email, String content, Date createdDate) {
        this.email = email;
        this.content = content;
        this.createdDate = createdDate;
    }
}
