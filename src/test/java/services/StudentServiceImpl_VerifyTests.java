package services;

import data.models.Gender;
import data.models.Student;
import data.repositories.HostelRepository;
import data.repositories.StudentRepository;
import dto.RegistrationRequest;
import dto.StudentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class StudentServiceImpl_VerifyTests {
    private StudentService studentService;
    @Spy
    private StudentRepository studentRepository;
    @Spy
    private HostelRepository hostelRepository;
    private LocalDateTime time;
    private Student student;

    @BeforeEach
    void setUp() {
        time = null;
        studentService = new StudentServiceImpl(studentRepository, hostelRepository);
    }

    @Test
    void test_assignBedSpaceToStudent() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "John",
                "Doe",
                "securedPassword",
                "MAT100419",
                Gender.MALE);

        StudentDto studentDto = studentService.registerStudent(registrationRequest);
        verify(studentRepository, times(1)).findById("MAT100419");
        reset(studentRepository);
        studentService.assignBedSpace(studentDto);
        verify(studentRepository, times(1)).findById("MAT100419");
        verify(hostelRepository, times(0)).returnAvailableFemaleSpace();
        verify(hostelRepository, times(1)).returnAvailableMaleSpace();
    }
}
