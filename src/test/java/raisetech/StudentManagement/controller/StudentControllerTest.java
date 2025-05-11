package raisetech.StudentManagement.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private StudentService service;

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  void 受講生詳細の一覧検索が実行できてからのリストがかえってくること() throws Exception {
    mockMvc.perform(get("/studentList"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    verify(service, times(1)).searchStudentList();
  }

  @Test
  void 受講生詳細の検索が出来て空で返ってくること() throws Exception {
    String id = "999";
    mockMvc.perform(get("/student/{id}", id))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudent(id);
  }

  @Test
  void 受講生詳細の登録が実行できて空で返ってくること()
      throws Exception {
    //リクエストデータは適切に構築して入力チェックの検証も兼ねている。
    //本来であれば返りは登録されたデータが入るが、モック化すると意味がないため、レスポンスは作らない。
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
  void 受講生詳細の更新ができて空で返ってくること()
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
        .andExpect(status().isOk());
//        .andExpect(content().string("更新処理が成功しました。"));

    verify(service, times(1)).updateStudent(any());
  }

//  @Test
//  void カタカナチェックでValidationExceptionが発生すること() throws Exception {
//    mockMvc.perform(post("/testRegister"))
//        .andExpect(status().isBadRequest())
//        .andExpect(result ->
//            assertThat(result.getResolvedException()).isInstanceOf(ValidationException.class));
//  }

//  @Test
//  void 受講生詳細の例外APIが実行できてステータスが400で返ってくること() throws Exception{
//    mockMvc.perform(get("/excpetion"))
//        .andExpect(status().is4xxClientError())
//        .andExpect(content().string("APIこのは現在利用できません。古いURLとなっています。"));
//  }


  @Test
  void 受講生詳細の受講生で適切な値を入力した時に入力チェックに異常が発生しないこと() {
    Student student = new Student();
    student.setId("1");
    student.setName("江並公史");
    student.setKanaName("エナミコウジ");
    student.setNickname("コウジ");
    student.setEmail("test@example.com");
    student.setArea("奈良");
    student.setSex("男性");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(0);

  }

  @Test
  void 受講生詳細の受講生でIDに数字以外を用いた時に入力チェックに掛かること() {
    Student student = new Student();
    student.setId("テストです。");
    student.setName("江並公史");
    student.setKanaName("エナミコウジ");
    student.setNickname("コウジ");
    student.setEmail("test@example.com");
    student.setArea("奈良");
    student.setSex("男性");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message")
        .containsOnly("数字のみ入力するようにしてください。");
  }

}