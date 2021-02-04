package ru.perm.v.redis1.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.perm.v.redis1.model.Student;

@Repository
public interface StudentRepository extends CrudRepository<Student, String> {
}