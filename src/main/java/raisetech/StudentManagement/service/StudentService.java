package raisetech.StudentManagement.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository){
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    return repository.search();

  }

  public StudentDetail searchStudent(String id) {
  Student student = repository.searchStudent(id);
  List<StudentsCourses> studentsCourses = repository.searchStudentsCourses(student.getId());
  StudentDetail studentDetail = new StudentDetail();
  studentDetail.setStudent(student);
  studentDetail.setStudentsCourses(studentsCourses);
  return studentDetail;
  }

  public List<StudentsCourses> searchStudentsCourseList() {
    return repository.searchStudentsCoursesList();
  }

  @Transactional
  public void registerStudent(StudentDetail studentDetail) {
    repository.registerStudent(studentDetail.getStudent());
    for (StudentsCourses studentsCourses : studentDetail.getStudentsCourses()) {
      studentsCourses.setStudentId(studentDetail.getStudent().getId());
      studentsCourses.setCourseStartAt(LocalDateTime.now());
      studentsCourses.setCourseEndAt(LocalDateTime.now().plusYears(1));
      repository.registerStudentsCourses(studentsCourses);

    }
  }

  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());
    for (StudentsCourses studentsCourse : studentDetail.getStudentsCourses()) {
//      studentsCourse.setStudentId(studentDetail.getStudent().getId());
      repository.updateStudentsCourses(studentsCourse);

    }
  }
}

//public List<Student> searchStudentList() {
//
//  List<Student> students = repository.search();
//  return students.stream()
//      .filter(student -> student.getAge() >= 30 && student.getAge() <=39)
//      .collect(Collectors.toList());
//}
//
//public List<StudentsCourses> searchStudentsCourseList() {
//
//  List<StudentsCourses> courses = repository.searchStudentsCourse();
//  return courses.stream()
//      .filter(course -> "Java".equalsIgnoreCase(course.getCourseName()))
//      .collect(Collectors.toList());
//}