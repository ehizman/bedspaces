package services;

import data.models.Student;
import dto.Register;
import dto.StudentDto;
import exceptions.HostelManagementException;

import java.util.List;

public interface StudentService {
    StudentDto registerStudent(Register studentDto) throws Exception;
    void assignBedSpace(StudentDto studentDto) throws Exception;
    List<String> returnNamesOfAllStudentsInAHostel(String hostelName) throws Exception;
    List<String> returnTheNamesOfAllStudentsInARoom(String roomId) throws Exception;
    Student findStudentById(String studentId) throws Exception;
}
