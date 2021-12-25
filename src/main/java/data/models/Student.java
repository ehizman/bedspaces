package data.models;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Student{
    private String firstName;
    private String lastName;
    private String password;
    private Gender gender;
    private LocalDateTime registrationTime;
    private String bedSpaceId;
    private String matricNo;

    public Student(String firstName, String lastName, String password,
                   String matricNo, Gender gender, LocalDateTime registrationTime) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.matricNo = matricNo;
        this.registrationTime = registrationTime;
        this.gender = gender;
    }

    public String getId() {
        return matricNo;
    }

    public String getName() {
        return firstName + " " + lastName;
    }


}
