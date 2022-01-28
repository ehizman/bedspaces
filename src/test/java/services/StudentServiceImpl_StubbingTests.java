package services;

import data.models.*;
import data.repositories.HostelRepository;
import data.repositories.StudentRepository;
import dto.RegistrationRequest;
import dto.StudentDto;
import exceptions.DuplicateIdException;
import exceptions.HostelManagementException;
import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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

        when(studentRepository.save(any(Student.class))).thenAnswer(answer -> {
            return Student.builder()
                    .firstName("John")
                    .lastName("Doe")
                    .matricNo("MAT100419")
                    .password("securedPassword")
                    .registrationTime(getTime())
                    .gender(Gender.MALE).build();
        });
        Student student = Student.builder()
                .firstName("John")
                .lastName("Doe")
                .matricNo("MAT100419")
                .password("securedPassword")
                .registrationTime(getTime())
                .gender(Gender.MALE).build();

        doAnswer(answer -> {
            Student arg = answer.getArgument(0);
            assertThat(arg, equalTo(student));
            return null;
        }).when(studentRepository).save(student);

        studentRepository.save(student);

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
        assertThatThrownBy(() -> studentService.registerStudent(registrationRequest))
                .isInstanceOf(DuplicateIdException.class)
                .hasMessage("student record with matric number already exists");
    }

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

    @Test
    void testThatThrowsHostelManagementExceptionWhenANullOrEmptyStringIsPassedAsArgument() throws Exception {
        when(studentRepository.findById(null)).thenThrow(new HostelManagementException("User id cannot be null"));
        assertThrows(HostelManagementException.class, () -> studentRepository.findById(null));
    }
}