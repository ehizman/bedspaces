package dto;

import data.models.Gender;
import data.models.Storable;
import lombok.*;


@Getter
@Setter
@RequiredArgsConstructor
public class StudentDto implements Storable {
    private String firstName;
    private String lastName;
    private String matricNo;
    private Gender gender;
    private String bedSpace;

    public StudentDto(String firstName, String lastName, String matricNo, Gender gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.matricNo = matricNo;
        this.gender = gender;
    }

    @Override
    public String getId() {
        return matricNo;
    }
}
