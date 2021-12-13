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
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Captor
    ArgumentCaptor<Student> studentArgumentCaptor;

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

    @Test
    void test_returnNamesOfStudentInARoom() throws Exception {
        RegistrationRequest firstRegistrationRequest = new RegistrationRequest(
                "John",
                "Doe",
                "securedPassword",
                "MAT100419",
                Gender.MALE);

        RegistrationRequest secondRegistrationRequest = new RegistrationRequest(
                "Mary",
                "Fallow",
                "securedPassword",
                "PSC100419",
                Gender.FEMALE);

        StudentDto firstStudentDto = studentService.registerStudent(firstRegistrationRequest);
        StudentDto secondStudentDto = studentService.registerStudent(secondRegistrationRequest);
        studentService.assignBedSpace(firstStudentDto);
        studentService.assignBedSpace(secondStudentDto);
        reset(studentRepository);//This reset is put here to re-initialize before interaction with returnTheNamesOfAllStudentsInARoom()
        List<String> studentNames = studentService.returnTheNamesOfAllStudentsInARoom("HALL3 Room 1");
        verify(studentRepository,never()).findById(anyString());
        verify(studentRepository, times(1)).findAll();
        assertThat(studentNames).contains("John Doe");
        studentNames = null;
        reset(studentRepository);
        studentNames = studentService.returnTheNamesOfAllStudentsInARoom("HALL1 Room 1");
        verify(studentRepository,never()).findById(anyString());
        verify(studentRepository, times(1)).findAll();
        assertThat(studentNames).contains("Mary Fallow");
    }

    @Test
    void testFindStudentById() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "John",
                "Doe",
                "securedPassword",
                "MAT100419",
                Gender.MALE);
        StudentDto studentDto = studentService.registerStudent(registrationRequest);
        studentService.assignBedSpace(studentDto);
        reset(studentRepository);
        Student student = studentService.findStudentById("MAT100419");
        verify(studentRepository, atLeastOnce()).findById(anyString());
        assertThat(student.getName()).isEqualTo("John Doe");
        assertThat(student.getPassword()).isEqualTo("securedPassword");
        assertThat(student.getMatricNo()).isEqualTo("MAT100419");
        assertThat(student.getGender()).isEqualTo(Gender.MALE);
    }

    @Test
    void testThatOnlyFindAllIsCalledOnStudentRepository_WhenWeTryToReturnNamesOfAllStudentsInARoom() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "John",
                "Doe",
                "securedPassword",
                "MAT100419",
                Gender.MALE);
        StudentDto studentDto = studentService.registerStudent(registrationRequest);
        studentService.assignBedSpace(studentDto);
        reset(studentRepository, hostelRepository);

        studentService.returnTheNamesOfAllStudentsInARoom("HALL3 Room 1");
        verify(studentRepository, only()).findAll();
    }

    @Test
    void testThatThereAreNoInteractionsWithHostelRepositoryWhenInvokingFindStudentById() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "John",
                "Doe",
                "securedPassword",
                "MAT100419",
                Gender.MALE);
        StudentDto studentDto = studentService.registerStudent(registrationRequest);
        studentService.assignBedSpace(studentDto);
        reset(studentRepository, hostelRepository);

        studentService.findStudentById("MAT100419");
        verifyNoInteractions(hostelRepository);
    }

    @Test
    void testThatThereAreNoMoreInteractionsWithMockObject() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "John",
                "Doe",
                "securedPassword",
                "MAT100419",
                Gender.MALE);
        StudentDto studentDto = studentService.registerStudent(registrationRequest);
        studentService.assignBedSpace(studentDto);
        reset(studentRepository, hostelRepository);
        verifyNoMoreInteractions(hostelRepository);
    }

    @Test
    void testTheOrderOfInteractions() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "John",
                "Doe",
                "securedPassword",
                "MAT100419",
                Gender.MALE);
        StudentDto studentDto = studentService.registerStudent(registrationRequest);
        studentService.assignBedSpace(studentDto);
        InOrder inorder = inOrder(studentRepository, hostelRepository);
        inorder.verify(studentRepository).findById(anyString());
        inorder.verify(hostelRepository).returnAvailableMaleSpace();
    }

    @Test
    void testTheOrderOfInteractionWithVerificationModePassedAsAnArgument() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "John",
                "Doe",
                "securedPassword",
                "MAT100419",
                Gender.MALE);
        StudentDto studentDto = studentService.registerStudent(registrationRequest);
        studentService.assignBedSpace(studentDto);
        InOrder inorder = inOrder(studentRepository, hostelRepository);
        inorder.verify(studentRepository, times(1)).findById(anyString());
        inorder.verify(hostelRepository, times(1)).returnAvailableMaleSpace();
        inorder.verify(hostelRepository, never()).findHostelByName(anyString());
    }

    @Test
    void capturingArgumentsUsingArgumentCaptor() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "John",
                "Doe",
                "securedPassword",
                "MAT100419",
                Gender.MALE);
        //The following lines asserts that our ModelMapper works as expected
        StudentDto studentDto = studentService.registerStudent(registrationRequest);
        verify(studentRepository).save(studentArgumentCaptor.capture());
        Student student = studentArgumentCaptor.getValue();
        assertThat(studentDto.getFirstName()).isEqualTo(student.getFirstName());
        assertThat(studentDto.getLastName()).isEqualTo(student.getLastName());
        assertThat(studentDto.getMatricNo()).isEqualTo(student.getMatricNo());
        assertThat(studentDto.getRegistrationTime()).isEqualTo(student.getRegistrationTime());
    }

    @Test
    void capturingArgumentsUsingArgumentCaptor_DemonstratingGetAllValues() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "John",
                "Doe",
                "securedPassword",
                "MAT100419",
                Gender.MALE);
        //The following lines asserts that our ModelMapper works as expected
        StudentDto studentDto = studentService.registerStudent(registrationRequest);
        verify(studentRepository).save(studentArgumentCaptor.capture());
        Student student = studentArgumentCaptor.getAllValues().get(0);
        assertThat(studentDto.getFirstName()).isEqualTo(student.getFirstName());
        assertThat(studentDto.getLastName()).isEqualTo(student.getLastName());
        assertThat(studentDto.getMatricNo()).isEqualTo(student.getMatricNo());
        assertThat(studentDto.getRegistrationTime()).isEqualTo(student.getRegistrationTime());
    }
}
