package ru.perm.v.redis1.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.perm.v.redis1.model.Student;
import ru.perm.v.redis1.service.StudentService;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentCtrl.class)
class StudentCtrlTest {

    private static final String STUDENT_PAGE = "studentpage";
    private static final String STUDENT_ATTR = "student";
    private static final String RESULT_ATTR = "result";
    private static final Integer ID = 1;
    private static final Integer ID_ERROR = 0;
    private static final String NAME = "NAME";
    private static final String NAME_ERROR = "";
    @MockBean
    StudentService studentService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void start() throws Exception {
        Student nullStudent = new Student();
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute(RESULT_ATTR, ""))
                .andExpect(model().attribute(STUDENT_ATTR, nullStudent))
                .andExpect(view().name(STUDENT_PAGE))
        ;
    }

    @Test
    void save() throws Exception {
        Student student = new Student();
        student.setId(ID);
        student.setName(NAME);
        when(studentService.getById(ID)).thenReturn(student);
        this.mockMvc.perform(post("/")
                .accept(MediaType.TEXT_HTML)
                .param("id", String.valueOf(ID))
                .param("name", NAME))
                .andExpect(model().errorCount(0))
                .andExpect(status().isOk())
                .andExpect(model().attribute(STUDENT_ATTR, student))
                .andExpect(model().attribute(RESULT_ATTR, containsString("Сохранено:")))
                .andExpect(model().attribute(RESULT_ATTR, containsString(student.toString())))
                .andExpect(view().name(STUDENT_PAGE))
        ;
        verify(studentService, times(1)).save(student);
        verify(studentService, times(1)).getById(ID);
    }

    @Test
    void saveOnErrorId() throws Exception {
        Student student = new Student();
        student.setId(ID_ERROR);
        student.setName(NAME);
        this.mockMvc.perform(post("/")
                .accept(MediaType.TEXT_HTML)
                .param("id", String.valueOf(ID_ERROR))
                .param("name", NAME))
                .andExpect(model().errorCount(1))
                .andExpect(status().isOk())
                .andExpect(model().attribute(STUDENT_ATTR, student))
                .andExpect(model().attribute(RESULT_ATTR, containsString("Ошибка:")))
                .andExpect(view().name(STUDENT_PAGE))
        ;
        verify(studentService, never()).save(student);
        verify(studentService, never()).getById(ID);
    }

    @Test
    void saveOnErrorName() throws Exception {
        Student student = new Student();
        student.setId(ID);
        student.setName(NAME_ERROR);
        this.mockMvc.perform(post("/")
                .accept(MediaType.TEXT_HTML)
                .param("id", String.valueOf(ID))
                .param("name", NAME_ERROR))
                .andExpect(model().errorCount(1))
                .andExpect(status().isOk())
                .andExpect(model().attribute(STUDENT_ATTR, student))
                .andExpect(model().attribute(RESULT_ATTR, containsString("Ошибка:")))
                .andExpect(view().name(STUDENT_PAGE))
        ;
        verify(studentService, never()).save(student);
        verify(studentService, never()).getById(ID);
    }
}