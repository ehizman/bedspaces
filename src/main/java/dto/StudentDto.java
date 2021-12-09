package dto;

import data.models.Gender;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto{
    private String firstName;
    private String lastName;
    private String matricNo;
    private LocalDateTime registrationTime;
    private Gender gender;
    private String bedSpace;

    public StudentDto(String firstName, String lastName, String matricNo, Gender gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.matricNo = matricNo;
        this.gender = gender;
    }

    public String getId() {
        return matricNo;
    }
}
