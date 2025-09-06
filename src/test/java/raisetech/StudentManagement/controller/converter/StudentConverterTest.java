package raisetech.StudentManagement.controller.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.data.StudentsCoursesStatuses;
import raisetech.StudentManagement.data.StudentsCoursesStatuses.Status;
import raisetech.StudentManagement.domain.StudentDetail;

class StudentConverterTest {

  private StudentConverter sut;

  @BeforeEach
  void before() {
    sut = new StudentConverter();
  }

  @Test
  void 受講生のリストと受講生コース情報のリストを渡して受講生詳細のリストが作成出来ること() {
    Student student = createStudent();

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId(1L);
    studentCourse.setStudentId("1");
    studentCourse.setCourseName("Javaコース");
    studentCourse.setCourseStartAt(LocalDateTime.now());
    studentCourse.setCourseEndAt(LocalDateTime.now().plusYears(1));

    StudentsCoursesStatuses status = new StudentsCoursesStatuses();
    status.setStudentCourseId(1L);
    status.setStatus(Status.本申込);

    List<Student> studentList = List.of(student);
    List<StudentCourse> studentCourseList = List.of(studentCourse);
    List<StudentsCoursesStatuses> statusList = List.of(status);

    List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList, statusList);

    assertThat(actual.get(0).getStudent()).isEqualTo(student);
    assertThat(actual.get(0).getStudentCourseList()).hasSize(1);
    assertThat(actual.get(0).getStudentCourseList().get(0).getStatusList()).containsExactly(status);
  }

  @Test
  void 受講生のリストと受講生コース情報のリストを渡した時に紐づかない受講生コース情報は除外されること() {
    Student student = createStudent();

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId(1L);
    studentCourse.setStudentId("2"); // 紐付かない studentId
    studentCourse.setCourseName("Javaコース");
    studentCourse.setCourseStartAt(LocalDateTime.now());
    studentCourse.setCourseEndAt(LocalDateTime.now().plusYears(1));

    // 一応ステータスを作るが紐付かない
    StudentsCoursesStatuses status = new StudentsCoursesStatuses();
    status.setStudentCourseId(1L);
    status.setStatus(Status.仮申込);

    List<Student> studentList = List.of(student);
    List<StudentCourse> studentCourseList = List.of(studentCourse);
    List<StudentsCoursesStatuses> statusList = List.of(status);

    List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList, statusList);

    assertThat(actual.get(0).getStudent()).isEqualTo(student);
    assertThat(actual.get(0).getStudentCourseList()).isEmpty();
    assertThat(actual.get(0).getStatusesList()).isNull();
  }

  private static Student createStudent() {
    Student student = new Student();
    student.setId("1");
    student.setName("江島康介");
    student.setKanaName("エシマコウスケ");
    student.setNickname("コウスケ");
    student.setEmail("test3@example.com");
    student.setArea("愛知");
    student.setAge(29);
    student.setSex("男性");
    student.setRemark("");
    student.setDeleted(false);
    return student;

  }

}
