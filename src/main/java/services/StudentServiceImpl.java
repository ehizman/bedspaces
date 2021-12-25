package services;

import config.ModelMapperConfig;
import data.models.*;
import data.repositories.HostelRepository;
import data.repositories.StudentRepository;
import dto.RegistrationRequest;
import dto.StudentDto;
import exceptions.DuplicateIdException;
import exceptions.HostelManagementException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class StudentServiceImpl implements StudentService{
    private final StudentRepository studentRepository;
    private final HostelRepository hostelRepository;

    public StudentServiceImpl(StudentRepository studentRepository, HostelRepository hostelRepository){
        this.studentRepository = studentRepository;
        this.hostelRepository = hostelRepository;
    }
    @Override
    public StudentDto registerStudent(@NonNull RegistrationRequest registrationRequest) throws Exception {
        Optional<Student> optionalStudent = studentRepository.findById(registrationRequest.matricNo());
        if (optionalStudent.isPresent()){
            throw new DuplicateIdException("student record with matric number already exists");
        }
        Student student = ModelMapperConfig.getMapper().map(registrationRequest, Student.class);
        student.setRegistrationTime(LocalDateTime.now());
        student = studentRepository.save(student);
        return ModelMapperConfig.getMapper().map(student, StudentDto.class);
    }


    @Override
    public StudentDto assignBedSpace(@NonNull StudentDto studentDto) throws Exception {
        Student student = studentRepository.findById(studentDto.getMatricNo()).orElseThrow(()->
                new HostelManagementException("student with specified matric number not found!"));

        if (student.getBedSpaceId() != null){
            throw new HostelManagementException("student cannot be assigned more than one bed-space");
        }
        BedSpace bedSpace;
        if(studentDto.getGender()== Gender.FEMALE){
            bedSpace = hostelRepository.returnAvailableFemaleSpace();
        }
        else{
            bedSpace = hostelRepository.returnAvailableMaleSpace();
        }
        student.setBedSpaceId(bedSpace.getId());
        bedSpace.setEmpty(false);
        return ModelMapperConfig.getMapper().map(student, StudentDto.class);
    }

    @Override
    public Student findStudentById(@NonNull @NotBlank String studentId) throws Exception {
        return studentRepository.findById(studentId).orElseThrow(()-> new HostelManagementException("Student with specified matric" +
                "number does not exist!"));
    }
    public List<String> returnNamesOfAllStudentsInAHostel(@NonNull @NotBlank String hostelName) throws Exception {
        List<String> studentNames = new ArrayList<>();
        Hostel hostel = hostelRepository.findHostelByName(hostelName);
        for (Student student: studentRepository.findAll()) {
            if (student.getBedSpaceId().contains(hostel.getName().name())){
                studentNames.add(student.getName());
            }
        }
        return studentNames;
    }

    public List<String> returnTheNamesOfAllStudentsInARoom(@NonNull @NotBlank String roomId) throws HostelManagementException {
        List<String> studentNames = new ArrayList<>();
        for (Student student: studentRepository.findAll()) {
            if (student.getBedSpaceId().contains(roomId)){
                studentNames.add(student.getName());
            }
        }
        return studentNames;
    }
}
