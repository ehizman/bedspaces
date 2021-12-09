package services;

import data.models.Gender;
import data.models.Student;
import data.repositories.HostelRepository;
import data.repositories.StudentRepository;
import dto.RegistrationRequest;
import dto.StudentDto;
import exceptions.NullEntityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class StudentServicePartialMockingTests {
    private StudentService studentService;
    @Spy
    private StudentRepository studentRepository;
    @Spy
    private HostelRepository hostelRepository;

    private RegistrationRequest firstRegistrationRequest;
    private RegistrationRequest secondRegistrationRequest;
    private RegistrationRequest thirdRegistrationRequest;
    private RegistrationRequest fourthRegistrationRequest;

    @BeforeEach
    void setUp() throws Exception {
//        studentRepository = new StudentRepository();
//        hostelRepository = new HostelRepository();
        studentService = new StudentServiceImpl(studentRepository, hostelRepository);
        firstRegistrationRequest = new RegistrationRequest(
                "Titobi",
                "Ligali",
                "securedPassword",
                "MAT100419",
                Gender.MALE);

        secondRegistrationRequest = new RegistrationRequest(
                "Emmanuel",
                "Okammy",
                "sassword",
                "MAT100456",
                Gender.MALE);

        thirdRegistrationRequest = new RegistrationRequest(
                "Oludami",
                "Ajadi",
                "wassword",
                "MAT100445",
                Gender.FEMALE);

        fourthRegistrationRequest = new RegistrationRequest(
                "Ise",
                "Faladeye",
                "cassword",
                "MAT100439",
                Gender.FEMALE);
        StudentDto firstStudent = studentService.registerStudent(firstRegistrationRequest);
        StudentDto secondStudent = studentService.registerStudent(secondRegistrationRequest);
        StudentDto thirdStudent = studentService.registerStudent(thirdRegistrationRequest);
        StudentDto fourthStudent = studentService.registerStudent(fourthRegistrationRequest);

        studentService.assignBedSpace(firstStudent);
        studentService.assignBedSpace(secondStudent);
        studentService.assignBedSpace(thirdStudent);
        studentService.assignBedSpace(fourthStudent);
    }
    @Test
    void testThrowsNullEntityExceptionWhenTryingToSaveANull(){
        assertThatThrownBy(()->studentRepository.save(null))
                .isInstanceOf(NullEntityException.class)
                .hasMessage("student object cannot be null");
    }
    @Test
    void testReturnAllTheStudentsInAHostel() throws Exception {
        List<String> studentsInHall3 = studentService.returnNamesOfAllStudentsInAHostel("HALL3");
        verify(studentRepository).findAll();

        List<String> studentsInHall1 = studentService.returnNamesOfAllStudentsInAHostel("HALL1");

        assertThat(studentsInHall3, hasSize(2));
        assertThat(studentsInHall3, hasItems("Emmanuel Okammy", "Titobi Ligali"));
        assertThat(studentsInHall1, hasSize(2));
        assertThat(studentsInHall1, hasItems("Oludami Ajadi", "Ise Faladeye"));

    }

    @Test
    void returnNamesOfAllStudentsInARoom() throws Exception {
        List<String> namesOfStudentsIn_Hall3_Room1 = studentService.returnTheNamesOfAllStudentsInARoom("HALL3 Room 1");
        assertThat(namesOfStudentsIn_Hall3_Room1, hasItems("Emmanuel Okammy", "Titobi Ligali"));
    }

    @Test
    void registerStudentTest() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest("Joshua", "Makinde",
                "my_password", "PSC1004396", Gender.MALE);
        Student student = Student.builder()
                .firstName("Joshua")
                .lastName("Makinde")
                .matricNo("PSC100419")
                .password("my_password")
                .gender(Gender.MALE).build();
        doReturn(student).when(studentRepository).save(any(Student.class));
        StudentDto studentDto = studentService.registerStudent(registrationRequest);
        assertThat(studentDto, hasProperty("firstName", equalTo("Joshua")));
        assertThat(studentDto, hasProperty("lastName", equalTo("Makinde")));
        assertThat(studentDto, hasProperty("matricNo", equalTo("PSC100419")));
        assertThat(studentDto, hasProperty("gender", equalTo(Gender.MALE)));
    }
}
