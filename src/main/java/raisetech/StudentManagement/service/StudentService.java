package raisetech.StudentManagement.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourse;
import raisetech.StudentManagement.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {

    List<Student> students = repository.search();
    return students.stream()
        .filter(student -> student.getAge() >= 30 && student.getAge() <=39)
        .collect(Collectors.toList());
  }

  public List<StudentsCourse> searchStudentsCourseList() {

    List<StudentsCourse> courses = repository.searchStudentsCourse();
    return courses.stream()
        .filter(course -> "Java".equalsIgnoreCase(course.getCourseName()))
        .collect(Collectors.toList());
  }
}
