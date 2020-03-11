package homedoctor.medicine.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
@RequiredArgsConstructor
public class MockData {

    @Id @GeneratedValue
    private Long id;

    private String username;

    private String street;

    private String phoneNumber;

    public void updateMockDate(String username, String street, String phoneNumber) {
        this.setUsername(username);
        this.setStreet(street);
        this.setPhoneNumber(phoneNumber);
    }

    @Builder
    public MockData(String username, String street, String phoneNumber) {
        this.username = username;
        this.street = street;
        this.phoneNumber = phoneNumber;
    }
}
