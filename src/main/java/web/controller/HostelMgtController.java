package web.controller;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.repositories.HostelRepository;
import data.repositories.StudentRepository;
import dto.RegistrationRequest;
import dto.StudentDto;
import lombok.extern.slf4j.Slf4j;
import services.StudentService;
import services.StudentServiceImpl;

import static spark.Spark.*;

@Slf4j
public class HostelMgtController {
    private static HostelRepository hostelRepository;
    private static StudentRepository studentRepository;
    private static StudentService studentService;

    public static void main(String[] args) {
        hostelRepository = new HostelRepository();
        studentRepository = new StudentRepository();
        studentService = new StudentServiceImpl(studentRepository, hostelRepository);
        path("/api/v1/students", ()->{
            post("/register", ((request, response) -> {
                ObjectMapper mapper = new ObjectMapper();
                RegistrationRequest registrationRequest = mapper.readValue(request.body(), RegistrationRequest.class);
                log.info("registration request --> {}",registrationRequest);
                StudentDto studentDto = studentService.registerStudent(registrationRequest);

                return mapper.writer(new DefaultPrettyPrinter()).writeValueAsString(studentDto);
            }));
        });
    }
}
