package ru.perm.v.redis1.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.perm.v.redis1.model.Student;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentRedisTemplateCtrl.class)
class StudentRedisTemplateCtrlTest {

    private static final String STUDENT_PAGE = "studentpage";
    private static final String STUDENT_ATTR = "student";
    private static final String RESULT_ATTR = "result";
    private static final Integer ID = 1;
    private static final Integer ID_ERROR = 0;
    private static final String NAME = "NAME";
    private static final String NAME_ERROR = "";
    private static final String STUDENT_JSON = "{\"id\":1,\"name\":\"NAME\"}";

    @MockBean
    private RedisTemplate<String, String> template;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        when(template.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void saveOverTemplate() throws Exception {
        Student student = new Student(ID, NAME);
        when(valueOperations.get(ID.toString())).thenReturn(STUDENT_JSON);
        this.mockMvc.perform(post("/save-over-template")
                .accept(MediaType.TEXT_HTML)
                .param("id", String.valueOf(ID))
                .param("name", NAME))
                .andExpect(model().errorCount(0))
                .andExpect(status().isOk())
                .andExpect(model().attribute(STUDENT_ATTR, student))
                .andExpect(model().attribute(RESULT_ATTR, containsString("Сохранено через RedisTemplate:")))
                .andExpect(model().attribute(RESULT_ATTR, containsString(STUDENT_JSON)))
                .andExpect(view().name(STUDENT_PAGE))
        ;
        verify(valueOperations, times(1)).set(ID.toString(), STUDENT_JSON);
        verify(valueOperations, times(1)).get(ID.toString());
    }

    @Test
    void saveOnErrorId() throws Exception {
        Student student = new Student(ID_ERROR, NAME);
        this.mockMvc.perform(post("/save-over-template")
                .accept(MediaType.TEXT_HTML)
                .param("id", String.valueOf(ID_ERROR))
                .param("name", NAME))
                .andExpect(model().errorCount(1))
                .andExpect(status().isOk())
                .andExpect(model().attribute(STUDENT_ATTR, student))
                .andExpect(model().attribute(RESULT_ATTR, containsString("Ошибка:")))
                .andExpect(view().name(STUDENT_PAGE))
        ;
        verify(valueOperations, never()).set(ID.toString(), STUDENT_JSON);
        verify(valueOperations, never()).get(ID.toString());
    }

    @Test
    void saveOnErrorName() throws Exception {
        Student student = new Student(ID, NAME_ERROR);
        this.mockMvc.perform(post("/save-over-template")
                .accept(MediaType.TEXT_HTML)
                .param("id", String.valueOf(ID))
                .param("name", NAME_ERROR))
                .andExpect(model().errorCount(1))
                .andExpect(status().isOk())
                .andExpect(model().attribute(STUDENT_ATTR, student))
                .andExpect(model().attribute(RESULT_ATTR, containsString("Ошибка:")))
                .andExpect(view().name(STUDENT_PAGE))
        ;
        verify(valueOperations, never()).set(ID.toString(), STUDENT_JSON);
        verify(valueOperations, never()).get(ID.toString());
    }
}