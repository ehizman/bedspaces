package services;

import config.ModelMapperConfig;
import data.models.*;
import data.repositories.HostelRepository;
import data.repositories.Repository;
import dto.Register;
import dto.StudentDto;
import exceptions.HostelManagementException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentServiceImpl implements StudentService{
    private Repository<Student> studentRepository;
    private HostelRepository hostelRepository;

    public StudentServiceImpl(Repository<Student> studentRepository, HostelRepository hostelRepository){
        this.studentRepository = studentRepository;
        this.hostelRepository = hostelRepository;
    }
    @Override
    public StudentDto registerStudent(Register studentDto) throws Exception {
        Optional<Student> optionalStudent = studentRepository.findById(studentDto.matricNo());
        if (optionalStudent.isPresent()){
            throw new HostelManagementException("Matric number is not unique");
        }
        Student student = ModelMapperConfig.getMapper().map(studentDto, Student.class);
        student = studentRepository.save(student);
        return ModelMapperConfig.getMapper().map(student, StudentDto.class);
    }

    @Override
    public void assignBedSpace(StudentDto studentDto) throws Exception {
        Student student = studentRepository.findById(studentDto.getId()).orElseThrow(()->
                new HostelManagementException("student with specified matric number not found!"));
        BedSpace bedSpace;
        if(studentDto.getGender()== Gender.FEMALE){
            bedSpace = hostelRepository.returnAvailableFemaleSpace();
        }
        else{
            bedSpace = hostelRepository.returnAvailableMaleSpace();
        }
        student.setBedSpaceId(bedSpace.getId());
        bedSpace.setEmpty(false);
    }

    @Override
    public Student findStudentById(String studentId) throws Exception {
        return studentRepository.findById(studentId).orElseThrow(()-> new HostelManagementException("Student with specified matric" +
                "number does not exist!"));
    }
    public List<String> returnNamesOfAllStudentsInAHostel(String hostelName) throws Exception {
        List<String> studentNames = new ArrayList<>();
        Hostel hostel = hostelRepository.findHostelByName(hostelName);
        for (Student student: studentRepository.findAll()) {
            if (student.getBedSpaceId().contains(hostel.getName().name())){
                studentNames.add(student.getName());
            }
        }
        return studentNames;
    }

    public List<String> returnTheNamesOfAllStudentsInARoom(String roomId) throws HostelManagementException {
        List<String> studentNames = new ArrayList<>();
        for (Student student: studentRepository.findAll()) {
            if (student.getBedSpaceId().contains(roomId)){
                studentNames.add(student.getName());
            }
        }
        return studentNames;
    }
}
