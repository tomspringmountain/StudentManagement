package raisetech.StudentManagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.data.StudentsCoursesStatuses;
import raisetech.StudentManagement.data.StudentsCoursesStatuses.Status;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;


@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;

//  @Mock
//  private StudentsCourseStatusMapper statusMapper; // ←追加

  @BeforeEach
  void before() {
    sut = new StudentService(repository, converter);
  }

  @Test
  void 受講生詳細の一覧検索_リポジトリとコンバーターの処理が適切に呼び出せていること() {
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    List<StudentsCoursesStatuses> statusesList = new ArrayList<>();
    when(repository.search()).thenReturn(studentList);
    when(repository.searchStudentCourseList()).thenReturn(studentCourseList);

    sut.searchStudentList();

    verify(repository, times(1)).search();
    verify(repository, times(1)).searchStudentCourseList();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList,statusesList);
  }

  @Test
  void 受講生詳細の検索_リポジトリの処理が適切に呼び出せていること() {
    String id = "999";
    Student student = new Student();
    student.setId(id);

    List<StudentCourse> courseList = new ArrayList<>();
    List<StudentsCoursesStatuses> statusesList = new ArrayList<>();

    // モック設定：現在の searchStudent() 実装に合わせる
    when(repository.searchStudent(id)).thenReturn(student);
    when(repository.searchStudentCourse(id)).thenReturn(courseList);
    when(repository.findAll()).thenReturn(statusesList);

    // 実行
    StudentDetail actual = sut.searchStudent(id);

    // verify：正しく呼び出されたか
    verify(repository, times(1)).searchStudent(id);
    verify(repository, times(1)).searchStudentCourse(id);
    verify(repository, times(1)).findAll(); // ← ここを searchStudentStatuses から修正！

    // アサート（内容一致の確認）
    assertThat(actual.getStudent()).isEqualTo(student);
    assertThat(actual.getStudentCourseList()).isEqualTo(courseList);
    assertThat(actual.getStatusesList()).isEqualTo(statusesList);
  }

  @Test
  void 受講生詳細の登録_リポジトリの処理が適切に呼び出されていること() {
    Student student = new Student();
    StudentCourse studentCourse = new StudentCourse();
    List<StudentCourse> studentCourseList = List.of(studentCourse);
    List<StudentsCoursesStatuses> statusesList = new ArrayList<>();
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList, statusesList);

    sut.registerStudent(studentDetail);

    verify(repository, times(1)).registerStudent(student);
    verify(repository, times(1)).registerStudentCourse(studentCourse);
  }

  @Test
  void 受講生詳細の登録_初期化処理が行われること() {
    String id = "999";
    Student student = new Student();
    student.setId(id);
    StudentCourse studentCourse = new StudentCourse();

    sut.initStudentsCourse(studentCourse, student.getId());

    assertEquals(id, studentCourse.getStudentId());
    assertEquals(LocalDateTime.now().getHour(), studentCourse.getCourseStartAt().getHour());
    assertEquals(LocalDateTime.now().plusYears(1).getYear(), studentCourse.getCourseEndAt().getYear());
  }

  @Test
  void 受講生詳細の更新_リポジトリの処理が適切に呼び出されていること() {
    Student student = new Student();
    StudentCourse studentCourse = new StudentCourse();
    List<StudentCourse> studentCourseList = List.of(studentCourse);
    List<StudentsCoursesStatuses> statusesList = new ArrayList<>();
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList, statusesList);

    sut.updateStudent(studentDetail);

    verify(repository, times(1)).updateStudent(student);
    verify(repository, times(1)).updateStudentCourse(studentCourse);
  }
  @Test
  void ステータス登録処理が正しく行われること() {
    Long studentCourseId = 1L;
    Status status = Status.本申込;

    StudentsCoursesStatuses expected = new StudentsCoursesStatuses();
    expected.setStudentCourseId(studentCourseId);
    expected.setStatus(status);

    // insert のモックがないのは void メソッドだから
    StudentsCoursesStatuses result = sut.insert(studentCourseId, status);

    verify(repository, times(1)).insert(any(StudentsCoursesStatuses.class));
    assertEquals(studentCourseId, result.getStudentCourseId());
    assertEquals(status, result.getStatus());
  }

  @Test
  void ステータスの取得が正しく行われること() {
    Long id = 1L;
    StudentsCoursesStatuses status = new StudentsCoursesStatuses();
    when(repository.findByStudentCourseId(id)).thenReturn(status);

    Optional<StudentsCoursesStatuses> result = sut.findByStudentCourseId(id);

    verify(repository).findByStudentCourseId(id);
    assertTrue(result.isPresent());
    assertEquals(status, result.get());
  }
  @Test
  void ステータス更新が正常に行われること() {
    Long id = 1L;
    Status newStatus = Status.仮申込;

    StudentsCoursesStatuses status = new StudentsCoursesStatuses();
    status.setStudentCourseId(id);
    status.setStatus(Status.仮申込);
    when(repository.findByStudentCourseId(id)).thenReturn(status);

    sut.update(id, newStatus);

    assertEquals(newStatus, status.getStatus());
    verify(repository).update(status);
  }
  @Test
  void ステータスが存在しない場合は例外がスローされること() {
    Long id = 999L;
    when(repository.findByStudentCourseId(id)).thenReturn(null);

    RuntimeException exception = assertThrows(RuntimeException.class,
        () -> sut.update(id, Status.仮申込));

    assertEquals("Status not found", exception.getMessage());
  }
  @Test
  void ステータス削除が正しく行われること() {
    Long id = 1L;

    sut.delete(id);

    verify(repository).delete(id);
  }

}

