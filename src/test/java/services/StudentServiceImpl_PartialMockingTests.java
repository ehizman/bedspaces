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

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockingDetails;


@ExtendWith(MockitoExtension.class)
public class StudentServiceImpl_PartialMockingTests {
    private StudentService studentService;
    @Spy
    private StudentRepository studentRepository;
    @Spy
    private HostelRepository hostelRepository;



    @BeforeEach
    void setUp() throws Exception {
        studentService = new StudentServiceImpl(studentRepository, hostelRepository);
    }
    @Test
    void testThrowsNullEntityExceptionWhenTryingToSaveANull(){
        assertThatThrownBy(()->studentRepository.save(null))
                .isInstanceOf(NullEntityException.class)
                .hasMessage("student object cannot be null");
    }
    @Test
    void testReturnNamesOfAllStudentsInAHostel() throws Exception {
        List<StudentDto> registeredStudents = registerStudents();
        for (StudentDto dto: registeredStudents) {
            studentService.assignBedSpace(dto);
        }
        List<String> studentsInHall3 = studentService.returnNamesOfAllStudentsInAHostel("HALL3");
        List<String> studentsInHall1 = studentService.returnNamesOfAllStudentsInAHostel("HALL1");

        assertThat(studentsInHall3, hasSize(2));
        assertThat(studentsInHall3, hasItems("John Doe", "Peter Rabbit"));
        assertThat(studentsInHall1, hasSize(2));
        assertThat(studentsInHall1, hasItems("Mary Fallow", "Patricia Lemon"));

    }


    private List<StudentDto> registerStudents() throws Exception {
        RegistrationRequest firstRegistrationRequest = new RegistrationRequest(
                "John",
                "Doe",
                "securedPassword",
                "MAT100419",
                Gender.MALE);
        RegistrationRequest secondRegistrationRequest = new RegistrationRequest(
                "Mary",
                "Fallow",
                "securedWassword",
                "MAT100420",
                Gender.FEMALE);
        RegistrationRequest thirdRegistrationRequest = new RegistrationRequest(
                "Peter",
                "Rabbit",
                "securedDassword",
                "MAT100421",
                Gender.MALE);
        RegistrationRequest fourthRegistrationRequest = new RegistrationRequest(
                "Patricia",
                "Lemon",
                "securedBassword",
                "MAT100422",
                Gender.FEMALE);
        StudentDto firstStudentDto = studentService.registerStudent(firstRegistrationRequest);
        StudentDto secondStudentDto = studentService.registerStudent(secondRegistrationRequest);
        StudentDto thirdStudentDto = studentService.registerStudent(thirdRegistrationRequest);
        StudentDto fourthStudentDto = studentService.registerStudent(fourthRegistrationRequest);

        return new ArrayList<>(List.of(firstStudentDto, secondStudentDto,
                thirdStudentDto, fourthStudentDto));
    }

    @Test
    void registerStudentTest() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest("John", "Fallow",
                "my_password", "PSC1004396", Gender.MALE);
        Student student = Student.builder()
                .firstName("John")
                .lastName("Fallow")
                .matricNo("PSC100419")
                .password("my_password")
                .gender(Gender.MALE).build();
        doReturn(student).when(studentRepository).save(any(Student.class));
        StudentDto studentDto = studentService.registerStudent(registrationRequest);
        assertThat(studentDto, hasProperty("firstName", equalTo("John")));
        assertThat(studentDto, hasProperty("lastName", equalTo("Fallow")));
        assertThat(studentDto, hasProperty("matricNo", equalTo("PSC100419")));
        assertThat(studentDto, hasProperty("gender", equalTo(Gender.MALE)));
    }
}
