package services;

import data.models.Student;
import dto.RegistrationRequest;
import dto.StudentDto;

import java.util.List;

public interface StudentService {
    StudentDto registerStudent(RegistrationRequest studentDto) throws Exception;
    StudentDto assignBedSpace(StudentDto studentDto) throws Exception;
    List<String> returnNamesOfAllStudentsInAHostel(String hostelName) throws Exception;
    List<String> returnTheNamesOfAllStudentsInARoom(String roomId) throws Exception;
    Student findStudentById(String studentId) throws Exception;
}
