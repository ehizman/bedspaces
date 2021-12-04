package data.models;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Student implements Storable {
    private String firstName;
    private String lastName;
    private String password;
    private Gender gender;
    private String bedSpaceId;
    private String matricNo;

    public Student(String firstName, String lastName, String password, String matricNo, Gender gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.matricNo = matricNo;
        this.gender = gender;
    }

    @Override
    public String getId() {
        return matricNo;
    }

    public String getName() {
        return firstName + " " + lastName;
    }
}
