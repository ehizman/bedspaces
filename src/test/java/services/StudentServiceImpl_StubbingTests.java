package services;

import data.models.*;
import data.repositories.HostelRepository;
import data.repositories.StudentRepository;
import dto.RegistrationRequest;
import dto.StudentDto;
import exceptions.DuplicateIdException;
import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceImpl_StubbingTests {
    private StudentService studentService;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private HostelRepository hostelRepository;
    private LocalDateTime time;

    @BeforeEach
    void setUp() {
        time = null;
        studentService = new StudentServiceImpl(studentRepository, hostelRepository);
    }


    @Test
    void registerStudentTest() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "John",
                "Doe",
                "securedPassword",
                "MAT100419",
                Gender.MALE);
        Student studentToSave = Student.builder()
                                .firstName("John")
                                .lastName("Doe")
                                .matricNo("MAT100419")
                                .password("securedPassword")
                                .registrationTime(LocalDateTime.now())
                                .gender(Gender.MALE).build();
        when(studentRepository.save(any(Student.class))).thenReturn(studentToSave);
        StudentDto studentDto = studentService.registerStudent(registrationRequest);
        assertThat(studentDto, hasProperty("firstName", equalTo("John")));
        assertThat(studentDto, hasProperty("lastName", equalTo("Doe")));
        assertThat(studentDto, hasProperty("matricNo", equalTo("MAT100419")));
        assertThat(studentDto, hasProperty("gender", equalTo(Gender.MALE)));
    }

    @Test
    void registerStudentTest_WithRegistrationTime() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "John",
                "Doe",
                "securedPassword",
                "MAT100419",
                Gender.MALE);

        when(studentRepository.save(any(Student.class))).thenAnswer(answer->{
            return Student.builder()
                    .firstName("John")
                    .lastName("Doe")
                    .matricNo("MAT100419")
                    .password("securedPassword")
                    .registrationTime(getTime())
                    .gender(Gender.MALE).build();
        });
        StudentDto studentDto = studentService.registerStudent(registrationRequest);
        assertThat(studentDto, hasProperty("firstName", equalTo("John")));
        assertThat(studentDto, hasProperty("lastName", equalTo("Doe")));
        assertThat(studentDto, hasProperty("matricNo", equalTo("MAT100419")));
        assertThat(studentDto, hasProperty("registrationTime", equalTo(time)));
        assertThat(studentDto, hasProperty("gender", equalTo(Gender.MALE)));
    }

    private LocalDateTime getTime() {
        time = LocalDateTime.now();
        return time;
    }

    @Test
    void testThrowDuplicateIdException() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "John",
                "Doe",
                "securedPassword",
                "MAT100419",
                Gender.MALE);
        Student student = Student.builder()
                .firstName("John")
                .lastName("Doe")
                .matricNo("MAT100419")
                .password("securedPassword")
                .gender(Gender.MALE).build();
        when(studentRepository.findById(anyString())).thenReturn(Optional.of(student));
        assertThatThrownBy(()->studentService.registerStudent(registrationRequest))
                .isInstanceOf(DuplicateIdException.class)
                .hasMessage("student record with matric number already exists");
    }

//    @Test
//    void testReturnNamesOfAllStudentsInAHostel() throws Exception {
//        List<StudentDto> registeredStudents = registerStudents();
//        for (StudentDto dto: registeredStudents) {
//            studentService.assignBedSpace(dto);
//        }
//        when(studentRepository.findAll()).thenCallRealMethod();
//        when(hostelRepository.findHostelByName(anyString())).thenCallRealMethod();
//        List<String> studentsInHall3 = studentService.returnNamesOfAllStudentsInAHostel("HALL3");
//        List<String> studentsInHall1 = studentService.returnNamesOfAllStudentsInAHostel("HALL1");
//
//        assertThat(studentsInHall3, hasSize(2));
//        assertThat(studentsInHall3, hasItems("John Doe", "Peter Rabbit"));
//        assertThat(studentsInHall1, hasSize(2));
//        assertThat(studentsInHall1, hasItems("Mary Fallow", "Patricia Lemon"));
//
//    }
//
//
//    private List<StudentDto> registerStudents() throws Exception {
//        RegistrationRequest firstRegistrationRequest = new RegistrationRequest(
//                "John",
//                "Doe",
//                "securedPassword",
//                "MAT100419",
//                Gender.MALE);
//        RegistrationRequest secondRegistrationRequest = new RegistrationRequest(
//                "Mary",
//                "Fallow",
//                "securedWassword",
//                "MAT100420",
//                Gender.FEMALE);
//        RegistrationRequest thirdRegistrationRequest = new RegistrationRequest(
//                "Peter",
//                "Rabbit",
//                "securedDassword",
//                "MAT100421",
//                Gender.MALE);
//        RegistrationRequest fourthRegistrationRequest = new RegistrationRequest(
//                "Patricia",
//                "Lemon",
//                "securedBassword",
//                "MAT100422",
//                Gender.FEMALE);
//        when(studentRepository.findById(anyString())).thenCallRealMethod();
//        when(studentRepository.save(any(Student.class))).thenCallRealMethod();
//        StudentDto firstStudentDto = studentService.registerStudent(firstRegistrationRequest);
//        StudentDto secondStudentDto = studentService.registerStudent(secondRegistrationRequest);
//        StudentDto thirdStudentDto = studentService.registerStudent(thirdRegistrationRequest);
//        StudentDto fourthStudentDto = studentService.registerStudent(fourthRegistrationRequest);
//
//        return new ArrayList<>(List.of(firstStudentDto, secondStudentDto,
//                thirdStudentDto, fourthStudentDto));
//    }

    @Test
    void testAssignBedSpaceToStudent() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "John",
                "Doe",
                "securedPassword",
                "MAT100419",
                Gender.MALE);
        Student student = Student.builder()
                .firstName("John")
                .lastName("Doe")
                .matricNo("MAT100419")
                .password("securedPassword")
                .gender(Gender.MALE).build();

        when(studentRepository.findById(anyString())).thenReturn(Optional.empty());
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        StudentDto studentDto = studentService.registerStudent(registrationRequest);
        when(studentRepository.findById(anyString())).thenReturn(Optional.of(student));
        when(hostelRepository.returnAvailableMaleSpace()).thenReturn(new BedSpace("HALL3 Room 1 Bedspace 1"));
        studentService.assignBedSpace(studentDto);
        assertThat(student.getBedSpaceId(), not(equalTo(null)));
        assertThat(student.getBedSpaceId(), equalTo("HALL3 Room 1 Bedspace 1"));
    }
}