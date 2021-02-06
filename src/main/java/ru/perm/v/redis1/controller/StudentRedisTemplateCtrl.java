package ru.perm.v.redis1.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.perm.v.redis1.model.Student;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("")
public class StudentRedisTemplateCtrl {

    private static final Logger LOG = LoggerFactory.getLogger(StudentRedisTemplateCtrl.class);
    private static final String STUDENT_PAGE = "studentpage";
    private static final String STUDENT_ATTR = "student";
    private static final String RESULT_ATTR = "result";

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private RedisTemplate<String, String> template;

    /**
     * Сохранение в Redis в виде json-строки с ключом ID
     * <pre>
     * {@code
     * 127.0.0.1:6379> get 1
     * "{\"id\":1,\"name\":\"STUDENT_NAME\"}"
     * 127.0.0.1:6379>
     * }
     * </pre>
     *
     * @param student - студент для сохранения
     */
    @PostMapping("/save-over-template")
    public String saveOverTemplate(
            @ModelAttribute(STUDENT_ATTR) @Valid Student student,
            BindingResult bindingResult, Model model) {
        LOG.info("{}", student);
        model.addAttribute(STUDENT_ATTR, student);
        if (bindingResult.hasErrors()) {
            model.addAttribute(RESULT_ATTR, String.format("Ошибка: %s", getError(bindingResult)));
            return STUDENT_PAGE;
        }
        String json;
        try {
            json = getJson(student);
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage());
            model.addAttribute(RESULT_ATTR, String.format("Ошибка: %s", e.getMessage()));
            return STUDENT_PAGE;
        }
        LOG.info("savedStudent: {}", student);
        template.opsForValue().set(student.getId().toString(), json);
        model.addAttribute(RESULT_ATTR,
                String.format("Сохранено через RedisTemplate: %s",
                        template.opsForValue().get(student.getId().toString())));
        return STUDENT_PAGE;
    }

    private String getJson(Student student) throws JsonProcessingException {
        return mapper.writeValueAsString(student);
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
