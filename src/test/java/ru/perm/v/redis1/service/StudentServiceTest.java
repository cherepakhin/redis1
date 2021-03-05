package ru.perm.v.redis1.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.perm.v.redis1.model.Student;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class StudentServiceTest extends AbstractContainerBaseTest {

    @Autowired
    StudentService studentService;

    @AfterEach
    void tearDown() {
        cleanCache();
    }

    @Test
    void saveAndGet() {
        Integer ID = 1;
        String NAME = "NAME";
        Student student = new Student(ID, NAME);
        studentService.save(student);
        assertEquals(student, studentService.getById(ID));

    }
}