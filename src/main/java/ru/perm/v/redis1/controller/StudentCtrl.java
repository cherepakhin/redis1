package ru.perm.v.redis1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.perm.v.redis1.model.Student;

import javax.validation.Valid;

@Controller
@RequestMapping("")
public class StudentCtrl {

    private static final Logger LOG = LoggerFactory.getLogger(StudentCtrl.class);
    private static final String STUDENT_PAGE = "studentpage";
    private static final String STUDENT_ATTR = "student";

    @GetMapping("/")
    public String start(Model model) {
        Student student = new Student();
        model.addAttribute(STUDENT_ATTR, student);
        return STUDENT_PAGE;
    }

    @PostMapping("/")
    public String save(
            @ModelAttribute(STUDENT_ATTR) @Valid Student student,
            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(STUDENT_ATTR, student);
            return STUDENT_PAGE;
        }
        LOG.info("{}",student);
        model.addAttribute(STUDENT_ATTR, student);
        return STUDENT_PAGE;
    }
}
