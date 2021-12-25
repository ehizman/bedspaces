package web.controller;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import data.repositories.HostelRepository;
import data.repositories.StudentRepository;
import dto.RegistrationRequest;
import dto.StudentDto;
import exceptions.HostelManagementException;
import lombok.extern.slf4j.Slf4j;
import services.StudentService;
import services.StudentServiceImpl;


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
