package services;

import data.models.*;
import data.repositories.HostelRepository;
import data.repositories.StudentRepository;
import dto.RegistrationRequest;
import dto.StudentDto;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.ArgumentMatchers.any;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {
    private StudentService studentService;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private HostelRepository hostelRepository;
    private RegistrationRequest registrationRequest;
    private Student student;

    @BeforeEach
    void setUp() {
        studentService = new StudentServiceImpl(studentRepository, hostelRepository);
        registrationRequest = new RegistrationRequest(
                "Titobi",
                "Ligali",
                "securedPassword",
                "MAT419",
                Gender.MALE);

        student = Student.builder()
                .firstName("Titobi")
                .lastName("Ligali")
                .matricNo("MAT419")
                .password("securedPassword")
                .gender(Gender.MALE).build();
    }


    @Test
    void registerStudentTest() throws Exception {
        when(studentRepository.save(any(Student.class))).thenReturn(student);
//        when(studentRepository.save(any(Student.class))).thenAnswer(student -> Student.builder()
//                .firstName("Titobi")
//                .lastName("Ligali")
//                .matricNo("MAT419")
//                .password("securedPassword")
//                .gender(Gender.MALE).build());
//        when(studentRepository.save(null)).thenThrow(NullPointerException.class);
        StudentDto studentDto = studentService.registerStudent(registrationRequest);
        assertThat(studentDto, hasProperty("firstName", equalTo("Titobi")));
        assertThat(studentDto, hasProperty("lastName", equalTo("Ligali")));
        assertThat(studentDto, hasProperty("matricNo", equalTo("MAT419")));
        assertThat(studentDto, hasProperty("gender", equalTo(Gender.MALE)));

    }

    @Test
    void assignBedSpaceToStudent() throws Exception {
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