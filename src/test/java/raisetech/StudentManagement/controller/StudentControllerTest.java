package raisetech.StudentManagement.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCoursesStatuses;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;
import raisetech.StudentManagement.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private StudentService service;
  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @MockBean
  private StudentConverter converter;

  @MockBean
  private StudentRepository studentRepository;


  @Test
  void 受講生詳細の一覧検索が実行できて空のリストが返ってくること() throws Exception{
    mockMvc.perform(get("/studentList"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    verify(service, times(1)).searchStudentList();

  }

  @Test
  void 指定IDで受講生詳細の検索ができ正常ステータスが返ってくること() throws Exception {
    String id = "999";
    mockMvc.perform(get("/student/{id}", id))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudent(id);
  }

  @Test
  void 受講生詳細を登録すると登録内容が返されること()
      throws Exception {
    mockMvc.perform(post("/registerStudent").contentType(MediaType.APPLICATION_JSON).content(
            """
                 {
                 "student": {
                     "name" : "秋山鉄二",
                     "kanaName" : "アキヤマテツジ",
                     "nickname" : "テツ",
                     "email" : "test3@example.com",
                     "area" : "秋田",
                     "age" : "30",
                     "sex" : "男",
                     "remark" : ""
                 },
                 "studentCourseList" : [
                    {
                        "courseName" : "Javaコース"
                    }
                 ]
                }
                """
        ))
        .andExpect(status().isOk());

    verify(service, times(1)).registerStudent(any());
  }

  @Test
  void 受講生詳細の更新が成功し成功メッセージが返されること()
      throws Exception {
    mockMvc.perform(put("/updateStudent").contentType(MediaType.APPLICATION_JSON).content(
            """
                {
                 "student": {
                     "id" : "9",
                     "name" : "江並公史",
                     "kanaName" : "エナミコウジ",
                     "nickname" : "コウジ",
                     "email" : "test@example.com",
                     "area" : "奈良",
                     "age" : "36",
                     "sex" : "男",
                     "remark" : "",
                     "isDeletednick" : "true"
                },
                 "studentCourseList" : [
                 ]
                }
                """
        ))
        .andExpect(status().isOk())
        .andExpect(content().string("更新処理が成功しました。"));

    verify(service, times(1)).updateStudent(any());
  }


  @Test
  void 受講生詳細の例外APIが実行できてステータスが400で返ってくること() throws Exception {
    mockMvc.perform(get("/exception"))
        .andExpect(status().is4xxClientError())
        .andExpect(content().string("このAPIは現在利用できません。古いURLとなっています。"));
  }


  @Test
  void 受講生詳細の受講生で適切な値を入力した時に入力チェックに異常が発生しないこと() {
    Student student = new Student(
        "1",
        "江並公史",
        "エナミコウジ",
        "コウジ",
        "test@example.com",
        "奈良",
        30,
        "男性",
        "",
        false
    );

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(0);

  }

  @Test
  void 受講生詳細の受講生でIDに数字以外を用いた時に入力チェックに掛かること() {
    Student student = new Student(
        "テストです",
        "江並公史",
        "エナミコウジ",
        "コウジ",
        "test@example.com",
        "奈良",
        30,
        "男性",
        "",
        false
    );

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message")
        .containsOnly("数字のみ入力するようにしてください。");
  }

  @Test
  void 例外APIが実行されて404エラーが返ること() throws Exception {
    mockMvc.perform(get("/exception"))
        .andExpect(status().isNotFound())
        .andExpect(content().string("このAPIは現在利用できません。古いURLとなっています。"));
  }



  @Test
  void ステータスが登録できて正常レスポンスが返ること() throws Exception {
    StudentsCoursesStatuses mockResponse = new StudentsCoursesStatuses(
        1L, 123L, StudentsCoursesStatuses.Status.仮申込, LocalDateTime.now());

    when(service.insert(eq(123L), eq(StudentsCoursesStatuses.Status.仮申込)))
        .thenReturn(mockResponse);

    mockMvc.perform(post("/studentsCoursesStatuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
              {
                "studentCourseId": 123,
                "status": "仮申込"
              }
          """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.studentCourseId").value(123))
        .andExpect(jsonPath("$.status").value("仮申込"));

    verify(service).insert(123L, StudentsCoursesStatuses.Status.仮申込);
  }

  @Test
  void ステータスが取得できること() throws Exception {
    StudentsCoursesStatuses status = new StudentsCoursesStatuses(
        1L, 123L, StudentsCoursesStatuses.Status.受講中, LocalDateTime.now());

    when(service.findByStudentCourseId(123L)).thenReturn(Optional.of(status));

    mockMvc.perform(get("/studentsCoursesStatuses/{studentCourseId}", 123L))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("受講中"));

    verify(service).findByStudentCourseId(123L);
  }

  @Test
  void ステータスが存在しないとき404が返ること() throws Exception {
    when(service.findByStudentCourseId(999L)).thenReturn(Optional.empty());

    mockMvc.perform(get("/studentsCoursesStatuses/{studentCourseId}", 999L))
        .andExpect(status().isNotFound());

    verify(service).findByStudentCourseId(999L);
  }

  @Test
  void ステータスが更新できること() throws Exception {
    mockMvc.perform(put("/studentsCoursesStatuses/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
              {
                "status": "受講終了"
              }
          """))
        .andExpect(status().isOk());

    verify(service).update(1L, StudentsCoursesStatuses.Status.受講終了);
  }


  @Test
  void ステータスが削除できること() throws Exception {
    mockMvc.perform(delete("/studentsCoursesStatuses/{id}", 1L))
        .andExpect(status().isOk())
        .andExpect(content().string("削除処理が成功しました。"));

    verify(service).delete(1L);
  }

}
