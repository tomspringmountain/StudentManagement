package raisetech.StudentManagement.service;

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
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    return repository.search();
  }

  public List<StudentsCourses> searchStudentsCourseList() {
    return repository.searchStudentsCourse();
  }
@Transactional
  public void registerStudent(StudentDetail studentDetail){
    repository.registerStudent(studentDetail.getStudent());
    //コース情報　Repository追加したらできる。
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