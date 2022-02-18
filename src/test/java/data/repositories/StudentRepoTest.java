package data.repositories;

import data.models.Gender;
import data.models.Student;
import exceptions.HostelManagementException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.constraints.Null;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentCaptor.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentRepoTest {
    @Mock
    StudentRepo studentRepo;
    @Captor
    ArgumentCaptor<Student> studentArgumentCaptor;
    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;
    @Test
    void testSaveMethod() throws HostelManagementException {
        Student student = Student.builder()
                .firstName("John")
                .lastName("Doe")
                .matricNo("MAT100419")
                .password("securedPassword")
                .registrationTime(LocalDateTime.now())
                .gender(Gender.MALE).build();
        when(studentRepo.save(any(Student.class), eq("John Doe"))).thenReturn(student);
        Student returnedStudent = studentRepo.save(student, "John Doe");
        System.out.println(returnedStudent);
        assertThat(returnedStudent, hasProperty("firstName", equalTo("John")));
        assertThat(returnedStudent, hasProperty("lastName", equalTo("Doe")));
        assertThat(returnedStudent, hasProperty("matricNo", equalTo("MAT100419")));
        assertThat(returnedStudent, hasProperty("gender", equalTo(Gender.MALE)));
    }

    /** To run this test case,
     you first have to make the `save` method in `StudentRepo` a void method
     **/

//    @Test
//    void testDoNothingDirective() throws HostelManagementException {
//        Student student = Student.builder()
//                .firstName("John")
//                .lastName("Doe")
//                .matricNo("MAT100419")
//                .password("securedPassword")
//                .gender(Gender.MALE)
//                .registrationTime(LocalDateTime.now())
//                .build();
//        doNothing().when(studentRepo).save(studentArgumentCaptor.capture(), stringArgumentCaptor.capture());
//        studentRepo.save(student, student.getName());
//        Student capturedStudent = studentArgumentCaptor.getValue();
//        String capturedString = stringArgumentCaptor.getValue();
//        assertThat(capturedStudent, hasProperty("firstName", equalTo("John")));
//        assertThat(capturedStudent, hasProperty("lastName", equalTo("Doe")));
//        assertThat(capturedStudent, hasProperty("matricNo", equalTo("MAT100419")));
//        assertThat(capturedStudent, hasProperty("gender", equalTo(Gender.MALE)));
//        assertThat(capturedString, equalTo("John Doe"));
//    }

    /** To run this test case,
        you first have to make the `save` method in `StudentRepo` a void method
     **/

//    @Test
//    void testDoNothingDirectiveWithVerify() throws HostelManagementException {
//        Student student = Student.builder()
//                .firstName("John")
//                .lastName("Doe")
//                .matricNo("MAT100419")
//                .password("securedPassword")
//                .gender(Gender.MALE)
//                .registrationTime(LocalDateTime.now())
//                .build();
//        doNothing().when(studentRepo).save(any(Student.class), eq("John Doe"));
//        studentRepo.save(student, student.getName());
//        verify(studentRepo, times(1)).save(student, student.getName());
//    }

    @Test
    void testDoThrow() throws HostelManagementException {
        doThrow(NullPointerException.class).when(studentRepo).save(any(), anyString());
        assertThrows(NullPointerException.class, ()->studentRepo.save(null, "John Doe"));
    }

    @Test
    void testThenThrow() throws HostelManagementException {
        when(studentRepo.save(any(), anyString())).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, ()->studentRepo.save(null, "John Doe"));
    }

    @Test
    void getMockDetails(){
        System.out.println(mockingDetails(studentRepo).isMock());
    }
}