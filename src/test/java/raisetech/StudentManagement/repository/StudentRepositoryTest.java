package raisetech.StudentManagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.data.StudentsCoursesStatuses;
import raisetech.StudentManagement.data.StudentsCoursesStatuses.Status;


@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void 受講生の全件検索が行えること() {
    List<Student> actual = sut.search();
    assertThat(actual.size()).isEqualTo(5);
  }

  @Test
  void 受講生の登録が行えること() {
    Student student = new Student();
    student.setName("江島康介");
    student.setKanaName("エシマコウスケ");
    student.setNickname("コウスケ");
    student.setEmail("test3@example.com");
    student.setArea("愛知");
    student.setAge(29);
    student.setSex("男性");
    student.setRemark("");
    student.setDeleted(false);

    sut.registerStudent(student);

    List<Student> actual = sut.search();

    assertThat(actual.size()).isEqualTo(6);
  }

  @Test
  void 受講生IDで検索できること() {
    Student actual = sut.searchStudent("1");

    assertThat(actual).isNotNull();
    assertThat(actual.getId()).isEqualTo("1");
    assertThat(actual.getName()).isEqualTo("山田太郎");
    assertThat(actual.getKanaName()).isEqualTo("ヤマダ タロウ");
    assertThat(actual.getNickname()).isEqualTo("タロー");
    assertThat(actual.getEmail()).isEqualTo("taro@example.com");
    assertThat(actual.getArea()).isEqualTo("東京");
    assertThat(actual.getAge()).isEqualTo(20);
    assertThat(actual.getSex()).isEqualTo("男性");
  }

  @Test
  void 存在しないIDで検索するとnullが返ること() {
    Student actual = sut.searchStudent("999");
    assertThat(actual).isNull();
  }

  @Test
  void 受講生のコース情報の全件検索が行えること() {
    List<StudentCourse> actual = sut.searchStudentCourseList();
    assertThat(actual.size()).isEqualTo(10);
  }

  @Test
  void 指定した受講生IDのコース情報を取得できること() {
    List<StudentCourse> actual = sut.searchStudentCourse("1");

    assertThat(actual).hasSize(3);
    assertThat(actual)
        .extracting(StudentCourse::getCourseName)
        .containsExactlyInAnyOrder("WebDesign", "Java", "Photo");

    assertThat(actual)
        .extracting(StudentCourse::getCourseStartAt)
        .isNotEmpty();
  }

  @Test
  void 受講生のコース登録が行えること() {

    StudentCourse course = new StudentCourse();
    course.setStudentId("1");
    course.setCourseName("Marketing");
    course.setCourseStartAt(LocalDateTime.now());
    course.setCourseEndAt(LocalDateTime.now().plusYears(1));

    sut.registerStudentCourse(course);

    List<StudentCourse> actual = sut.searchStudentCourse("1");
    assertThat(actual.size()).isEqualTo(4);

    assertThat(actual)
        .extracting(StudentCourse::getCourseName)
        .contains("Marketing");
  }


  @Test
  void 受講生の情報が更新できること() {
    Student student = sut.searchStudent("1");

    student.setName("江島康介");
    student.setKanaName("エシマコウスケ");
    student.setNickname("コウスケ");
    student.setEmail("test3@example.com");
    student.setArea("愛知");
    student.setAge(29);
    student.setSex("男性");
    student.setRemark("");
    student.setDeleted(false);

    sut.updateStudent(student);

    Student updated = sut.searchStudent("1");

    assertThat(updated.getName()).isEqualTo("江島康介");
    assertThat(updated.getEmail()).isEqualTo("test3@example.com");
  }

  @Test
  void 受講生コースのコース名が更新できること() {
    List<StudentCourse> courses = sut.searchStudentCourse("1");

    StudentCourse targetCourse = courses.stream()
        .filter(c -> c.getCourseName().equals("Java"))
        .findFirst()
        .orElseThrow(() -> new AssertionError("対象のコースが見つかりません"));

    targetCourse.setCourseName("English");

    sut.updateStudentCourse(targetCourse);

    List<StudentCourse> updatedCourses = sut.searchStudentCourse("1");

    boolean isUpdated = updatedCourses.stream()
        .anyMatch(c -> c.getCourseName().equals("English"));

    assertThat(isUpdated).isTrue();
  }

//  @Test
//  void 学生IDからステータス情報が取得できること() {
//    List<StudentsCoursesStatuses> actual = sut.searchStudentStatuses("1");
//
//    assertThat(actual).isNotEmpty();
//    assertThat(actual)
//        .extracting(StudentsCoursesStatuses::getStatus)
//        .containsAnyOf(Status.仮申込, Status.本申込);
//  }

  @Test
  void studentCourseIdからステータス情報が1件取得できること() {
    StudentsCoursesStatuses actual = sut.findByStudentCourseId(101L); // 例: 101 は DB に存在する ID

    assertThat(actual).isNotNull();
    assertThat(actual.getStudentCourseId()).isEqualTo(101L);
    assertThat(actual.getStatus()).isIn(Status.仮申込, Status.本申込);
  }

  @Test
  void ステータスを新規登録できること() {
    StudentsCoursesStatuses newStatus = new StudentsCoursesStatuses();
    newStatus.setStudentCourseId(104L); // 存在する student_course_id を指定
    newStatus.setStatus(Status.仮申込);
    newStatus.setUpdatedAt(LocalDateTime.now());

    sut.insert(newStatus);

    StudentsCoursesStatuses inserted = sut.findByStudentCourseId(104L);
    assertThat(inserted).isNotNull();
    assertThat(inserted.getStatus()).isEqualTo(Status.仮申込);
  }

  @Test
  void ステータスを更新できること() {
    StudentsCoursesStatuses status = sut.findByStudentCourseId(102L); // 例: 事前に存在しているもの

    status.setStatus(Status.本申込);
    status.setUpdatedAt(LocalDateTime.now());

    sut.update(status);

    StudentsCoursesStatuses updated = sut.findByStudentCourseId(102L);
    assertThat(updated.getStatus()).isEqualTo(Status.本申込);
  }

  @Test
  void ステータスを削除できること() {
    StudentsCoursesStatuses status = sut.findByStudentCourseId(103L); // 存在する ID を想定

    assertThat(status).isNotNull();

    sut.delete(status.getId());

//    StudentsCoursesStatuses deleted = sut.findByStudentCourseId(104L);
//    assertThat(deleted).isNull();
    List<StudentsCoursesStatuses> remaining = sut.findAll();
    assertThat(remaining).noneMatch(s -> s.getStudentCourseId().equals(103L));

  }

}
