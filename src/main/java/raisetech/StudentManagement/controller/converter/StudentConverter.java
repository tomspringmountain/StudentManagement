package raisetech.StudentManagement.controller.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.data.StudentsCoursesStatuses;
import raisetech.StudentManagement.domain.StudentDetail;

/**
 * 受講生と受講生コース情報を受講生詳細に変換するコンバーターです。
 * 受講生詳細を受講生や受講生コース情報、もしくはその逆の変換を行うコンバーターです。
 */
@Component
public class StudentConverter {

  /**
   * 受講生に紐づく受講生コース情報をマッピングする 受講生コース情報は受講生に対して複数存在するのでループを回して受講生詳細情報を組み立てる。
   *
   * @param studentsList       　受講生一覧
   * @param studentsCourseList 　受講生コース情報のリスト
   * @return 受講生詳細情報のリスト
   */
  public List<StudentDetail> convertStudentDetails(
      List<Student> studentsList,
      List<StudentCourse> studentsCourseList,
      List<StudentsCoursesStatuses> statuses
  ) {
    List<StudentDetail> result = new ArrayList<>();

    for (Student student : studentsList) {
      StudentDetail detail = new StudentDetail();
      detail.setStudent(student);

      // この student に属するコースを抽出
      List<StudentCourse> courseList = studentsCourseList.stream()
          .filter(course -> Objects.equals(course.getStudentId(), student.getId()))
          .collect(Collectors.toList());

      // 各 course に statusList をセット
      for (StudentCourse course : courseList) {
        List<StudentsCoursesStatuses> courseStatuses = statuses.stream()
            .filter(status -> Objects.equals(status.getStudentCourseId(), course.getId()))
            .collect(Collectors.toList());
        course.setStatusList(courseStatuses);
      }

      detail.setStudentCourseList(courseList);
      result.add(detail);
    }

    return result;
  }
}
