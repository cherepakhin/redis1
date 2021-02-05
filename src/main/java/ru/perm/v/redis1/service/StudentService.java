package ru.perm.v.redis1.service;

import ru.perm.v.redis1.model.Student;
import ru.perm.v.redis1.repository.StudentRepository;

public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public void save(Student student) {
        studentRepository.save(student);
    }

    public Student getById(Integer id) {
        return studentRepository.findById(id).orElse(new Student());
    }
}
