package web.controller;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import config.ModelMapperConfig;
import data.models.Student;
import data.repositories.HostelRepository;
import data.repositories.StudentRepository;
import dto.RegistrationRequest;
import dto.StudentDto;
import exceptions.HostelManagementException;
import lombok.extern.slf4j.Slf4j;
import services.StudentService;
import services.StudentServiceImpl;
import java.util.List;


import static spark.Spark.*;

@Slf4j
public class HostelMgtController {
    private static HostelRepository hostelRepository;
    private static StudentRepository studentRepository;
    private static StudentService studentService;
    private static ObjectMapper objectMapper;


    public static void main(String[] args) {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        hostelRepository = new HostelRepository();
        studentRepository = new StudentRepository();
        studentService = new StudentServiceImpl(studentRepository, hostelRepository);
        redirect.post("/register", "/assign-bed-space");

        path("/api/v1/students", ()->{
            post("/register", ((request, response) -> {
                RegistrationRequest registrationRequest = objectMapper.readValue(request.body(), RegistrationRequest.class);
                StudentDto studentDto = studentService.registerStudent(registrationRequest);
                return objectMapper.writer(new DefaultPrettyPrinter()).writeValueAsString(studentDto);
            }));
            get("/get-student-info/:studentId", (request, response)->{
                String studentId = request.params(":studentId");
                try{
                    Student student = studentService.findStudentById(studentId);
                    StudentDto studentDto = ModelMapperConfig.getMapper().map(student, StudentDto.class);
                    return objectMapper.writer(new DefaultPrettyPrinter()).writeValueAsString(studentDto);
                }catch (HostelManagementException exception){
                    log.info("Exception occurred --> {}", exception.getMessage());
                    return objectMapper.writer(new DefaultPrettyPrinter()).writeValueAsString(exception.getMessage());
                }
            });
            get("/get-names-of-students-in-hostel/:hostelName", (request, response) -> {
                String hostelName = request.params(":hostelName");
                try{
                    List<String> names = studentService.returnNamesOfAllStudentsInAHostel(hostelName);
                    return objectMapper.writer(new DefaultPrettyPrinter()).writeValueAsString(names);
                }catch (HostelManagementException exception){
                    log.info("Exception occurred --> {}", exception.getMessage());
                    return objectMapper.writer(new DefaultPrettyPrinter()).writeValueAsString(exception.getMessage());
                }
            });
        });
        path("api/v1/bed-spaces", ()->{
            post("/assign-bed-space", ((request, response) -> {
                response.type("application/json");
                StudentDto studentDto = objectMapper.readValue(request.body(), StudentDto.class);

                try{
                    StudentDto studentDto1 = studentService.assignBedSpace(studentDto);

                    return objectMapper.writer(new DefaultPrettyPrinter()).writeValueAsString(studentDto1);
                }
                catch(HostelManagementException exception){
                    log.info("Exception --> {}", exception.getMessage());
                    return objectMapper.writer(new DefaultPrettyPrinter()).writeValueAsString(exception.getMessage());
                }
            }));
        });
    }
}
