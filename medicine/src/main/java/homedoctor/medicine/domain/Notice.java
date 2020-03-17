package homedoctor.medicine.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@RequiredArgsConstructor
@Table(name = "notice")
public class Notice {

    @Id @GeneratedValue
    @Column(name = "notice_id")
    private Long noticeId;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_date")
    private Date createdDate;


    @Builder
    public Notice(Long id, String title, String content) {
        this.noticeId = id;
        this.title = title;
        this.content = content;
    }
}
