package raisetech.StudentManagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.exception.TestException;
import raisetech.StudentManagement.service.StudentService;

@Slf4j

/**
 * 受講生の検索や登録、更新などを行うREST APIとして受け付けるControllerです。
 */
@Validated
@RestController
public class StudentController {

  private StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 受講生詳細一覧検索です。
   * 全件検索を行うので、条件指定は行いません。
   *
   * @return 受講生詳細一覧(全件)
   */
//  @Operation(summary = "一覧検索" , description = "受講生の一覧を検索します。")
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() throws TestException {
//    throw new TestException(
//        "現在このAPIは利用できません。URLは「studentList」ではなく「students」を利用してください。");
   return service.searchStudentList();
  }

  /**
   * 受講生詳細の検索です。
   * ＩＤに紐づく任意の受講生の情報を取得します。
   *
   * @param id　受講生ＩＤ
   * @return 受講生
   */
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(
      @PathVariable @NotBlank @Pattern(regexp = "^\\d+$") String id) {
    return service.searchStudent(id);
  }

  /**
   * 受講生詳細の登録を行います。
   *
   * @param studentDetail　受講生詳細
   * @return 実行結果
   */
//  @Operation(summary = "受講生登録" , description = "受講生を登録します。")
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
    StudentDetail responseStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

  /**
   * 受講生詳細の更新を行います。
   * キャンセルフラグの更新もここで行います(論理削除)
   *
   * @param studentDetail　受講生詳細
   * @return 実行結果
   */
  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }

//  @ExceptionHandler(TestException.class)
//  public  ResponseEntity<String> handleTestException(TestException ex){
//    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
//  }
//@PostMapping("/registerStudent")
//public ResponseEntity<String> ValidationException(String message) {
//  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("カタカナで入力してください。");
//}
@PostMapping("/testRegister")
public ResponseEntity<String> register()
    throws ValidationException {
  // ここで独自のロジックによってエラーを投げることも可能
//  if (!student.getKanaName().matches("^[ァ-ヶー　]+$"))
    throw new ValidationException("フリガナはカタカナで入力してください。");

//  return ResponseEntity.ok("登録成功しました。");
}

}
