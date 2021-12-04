package services;

import data.models.Gender;
import data.models.Student;
import data.repositories.HostelRepository;
import data.repositories.Repository;
import data.repositories.StudentRepositoryImpl;
import dto.Register;
import dto.StudentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class StudentServiceConcreteTests {
    private StudentService studentService;
    private Repository<Student> studentRepository;
    private HostelRepository hostelRepository;

    private Register firstRegistrationRequest;
    private Register secondRegistrationRequest;
    private Register thirdRegistrationRequest;
    private Register fourthRegistrationRequest;

    @BeforeEach
    void setUp() throws Exception {
        studentRepository = new StudentRepositoryImpl();
        hostelRepository = new HostelRepository();
        studentService = new StudentServiceImpl(studentRepository, hostelRepository);
        firstRegistrationRequest = new Register(
                "Titobi",
                "Ligali",
                "securedPassword",
                "MAT100419",
                Gender.MALE);

        secondRegistrationRequest = new Register(
                "Emmanuel",
                "Okammy",
                "sassword",
                "MAT100456",
                Gender.MALE);

        thirdRegistrationRequest = new Register(
                "Oludami",
                "Ajadi",
                "wassword",
                "MAT100445",
                Gender.FEMALE);

        fourthRegistrationRequest = new Register(
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
    void testReturnAllTheStudentsInAHostel() throws Exception {
        List<String> studentsInHall3 = studentService.returnNamesOfAllStudentsInAHostel("HALL3");
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
}
