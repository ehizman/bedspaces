package doing_more_with_mockito;

import data.models.Student;
import exceptions.HostelManagementException;
import exceptions.NullEntityException;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class StudentRepo {
    private Map<String, Student> database = new HashMap<>();

    private void validate(String id) throws HostelManagementException {
        if (id == null || id.trim().equals("")){
            throw new HostelManagementException("provided an invalid id");
        }
    }

    public Student save(Student student, String name) throws HostelManagementException {
        if (student == null){
            throw new NullEntityException("student object cannot be null");
        }
        log.info("Student {} saved into the database", name);
        database.put(student.getId(), student);
        return student;
    }
}
