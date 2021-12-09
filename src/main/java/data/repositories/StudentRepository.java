package data.repositories;

import data.models.Student;
import exceptions.HostelManagementException;

import java.util.*;
import java.util.stream.Collectors;

public class StudentRepository{
    private Map<String, Student> database = new HashMap<>();

    private void validate(String id) throws HostelManagementException {
        if (id == null || id.trim().equals("")){
            throw new HostelManagementException("provided an invalid id");
        }
    }

    public Optional<Student> findById(String id) throws Exception {
        validate(id);
        Student student = database.getOrDefault(id, null);
        if (student == null){
            return Optional.empty();
        }
        return Optional.of(student);
    }

    public List<Student> findByName(String name) {
        return database.values().stream()
                .filter(student -> (student.getFirstName()+" "+student.getLastName()).contains(name))
                .collect(Collectors.toList());
    }

    public Student save(Student student) {
        database.put(student.getId(), student);
        return student;
    }

    public void delete(String id) {
        database.remove(id);
    }

    public void delete(Student student) {
        database.remove(student.getId(), student);
    }

    public List<Student> findAll() {
        return new ArrayList<>(database.values());
    }
}
