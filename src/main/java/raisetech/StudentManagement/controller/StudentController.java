package raisetech.StudentManagement.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import raisetech.StudentManagement.data.StudentsCoursesStatuses;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.exception.NotFoundException;
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
   * 受講生詳細一覧検索です。 全件検索を行うので、条件指定は行いません。
   *
   * @return 受講生詳細一覧(全件)
   */
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() throws TestException {

    return service.searchStudentList();
  }

  /**
   * 受講生詳細の検索です。 ＩＤに紐づく任意の受講生の情報を取得します。
   *
   * @param id 　受講生ＩＤ
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
   * @param studentDetail 　受講生詳細
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
   * 受講生詳細の更新を行います。 キャンセルフラグの更新もここで行います(論理削除)
   *
   * @param studentDetail 　受講生詳細
   * @return 実行結果
   */
  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }



  @GetMapping("/exception")
  public ResponseEntity<String> triggerException() throws NotFoundException {
    throw new NotFoundException("このAPIは現在利用できません。古いURLとなっています。");
  }

//  @PostMapping("/studentsCoursesStatuses")
//  public ResponseEntity<StudentsCoursesStatuses> insertStatus(
//      @RequestBody StudentsCoursesStatuses status) {
////    System.out.println(" POST 受信: " + status);
//    StudentsCoursesStatuses inserted = service.insert(
//        status.getStudentCourseId(),
//        status.getStatus()
//    );
//    return ResponseEntity.ok(inserted);
//  }

@PostMapping("/studentsCoursesStatuses")
public ResponseEntity<StudentsCoursesStatuses> insertStatus(
    @RequestBody StudentsCoursesStatuses status) {

  // ★ここに入れる！
  System.out.println("受信したstatus: " + status);
  System.out.println("studentCourseId: " + status.getStudentCourseId());
  System.out.println("status: " + status.getStatus());

  StudentsCoursesStatuses inserted = service.insert(
      status.getStudentCourseId(),
      status.getStatus()
  );
  return ResponseEntity.ok(inserted);
}


  @GetMapping("/studentsCoursesStatuses/{studentCourseId}")
  public ResponseEntity<StudentsCoursesStatuses> getStatus(@PathVariable Long studentCourseId) {
    return service.findByStudentCourseId(studentCourseId)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/studentsCoursesStatuses/{id}")
  public ResponseEntity<String> updateStatus(@PathVariable Long id,
      @RequestBody StudentController.StatusUpdateRequest request) {
    service.update(id, request.getStatus());
    return ResponseEntity.ok("更新処理が成功しました。");
  }

  @DeleteMapping("/studentsCoursesStatuses/{id}")
  public ResponseEntity<String> deleteStatus(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.ok("削除処理が成功しました。");
  }


  @Data
  public static class StatusUpdateRequest {

    @NotNull
    private StudentsCoursesStatuses.Status status;
  }

  @Data
  public static class StatusRequest {

    @NotNull
    private Long studentCourseId;
    @NotNull
    private StudentsCoursesStatuses.Status status;
  }

}


