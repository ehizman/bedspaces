package data.repositories;

import data.models.Student;
import exceptions.HostelManagementException;

import java.util.*;
import java.util.stream.Collectors;

public class StudentRepositoryImpl implements Repository<Student> {
    private Map<String, Student> database = new HashMap<>();

    private void validate(String id) throws HostelManagementException {
        if (id == null || id.trim().equals("")){
            throw new HostelManagementException("provided an invalid id");
        }
    }

    @Override
    public Optional<Student> findById(String id) throws Exception {
        validate(id);
        Student student = database.getOrDefault(id, null);
        if (student == null){
            return Optional.empty();
        }
        return Optional.of(student);
    }

    @Override
    public List<Student> findByName(String name) {
        return database.values().stream()
                .filter(student -> (student.getFirstName()+" "+student.getLastName()).contains(name))
                .collect(Collectors.toList());
    }

    @Override
    public Student save(Student student) {
        database.put(student.getId(), student);
        return student;
    }

    @Override
    public void delete(String id) {
        database.remove(id);
    }

    @Override
    public void delete(Student student) {
        database.remove(student.getId(), student);
    }

    @Override
    public List<Student> findAll() {
        return new ArrayList<>(database.values());
    }
}
