package ru.perm.v.redis1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.perm.v.redis1.model.Student;
import ru.perm.v.redis1.service.StudentService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("")
public class StudentCtrl {

    private static final Logger LOG = LoggerFactory.getLogger(StudentCtrl.class);
    private static final String STUDENT_PAGE = "studentpage";
    private static final String STUDENT_ATTR = "student";
    private static final String RESULT_ATTR = "result";

    @Autowired
    StudentService studentService;

    @GetMapping("/")
    public String start(Model model) {
        Student student = new Student();
        model.addAttribute(STUDENT_ATTR, student);
        model.addAttribute(RESULT_ATTR, "");
        return STUDENT_PAGE;
    }

    @PostMapping("/")
    public String save(
            @ModelAttribute(STUDENT_ATTR) @Valid Student student,
            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(STUDENT_ATTR, student);
            model.addAttribute(RESULT_ATTR, String.format("Ошибка: %s", getError(bindingResult)));
            return STUDENT_PAGE;
        }
        LOG.info("{}", student);
        studentService.save(student);
        Student savedStudent = studentService.getById(student.getId());
        LOG.info("savedStudent: {}", student);
        model.addAttribute(STUDENT_ATTR, student);
        model.addAttribute(RESULT_ATTR, String.format("Сохранено: %s", savedStudent));
        return STUDENT_PAGE;
    }

    String getError(BindingResult bindingResult) {
        StringBuilder bld = new StringBuilder();
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors) {
            bld.append(String.format("%s: %s%n", error.getObjectName(), error.getDefaultMessage()));
        }
        return bld.toString();
    }
}
