package ru.perm.v.redis1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.perm.v.redis1.model.Student;
import ru.perm.v.redis1.service.StudentService;

import javax.validation.Valid;

@Controller
@RequestMapping("")
public class StudentCtrl {

    private static final Logger LOG = LoggerFactory.getLogger(StudentCtrl.class);
    private static final String STUDENT_PAGE = "studentpage";
    private static final String STUDENT_ATTR = "student";
    private static final String STUDENT_RESULT_ATTR = "result_student";

    @Autowired
    StudentService studentService;

    @GetMapping("/")
    public String start(Model model) {
        Student student = new Student();
        model.addAttribute(STUDENT_ATTR, student);
        model.addAttribute(STUDENT_RESULT_ATTR, student);
        return STUDENT_PAGE;
    }

    @PostMapping("/")
    public String save(
            @ModelAttribute(STUDENT_ATTR) @Valid Student student,
            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Student nullStudent = new Student();
            model.addAttribute(STUDENT_ATTR, student);
            model.addAttribute(STUDENT_RESULT_ATTR, nullStudent);
            return STUDENT_PAGE;
        }
        LOG.info("{}", student);
        studentService.save(student);
        Student savedStudent = studentService.getById(student.getId());
        LOG.info("savedStudent: {}", student);
        model.addAttribute(STUDENT_ATTR, student);
        model.addAttribute(STUDENT_RESULT_ATTR, savedStudent);
        return STUDENT_PAGE;
    }
}
