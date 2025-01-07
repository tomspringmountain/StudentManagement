package raisetech.StudentManagement;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentManagementApplication {

  private Map<String, String> student = new HashMap<>();

  public static void main(String[] args) {
    SpringApplication.run(StudentManagementApplication.class, args);
  }

  @GetMapping("/studentInfo")
  public Map<String, String> getstudentInfo() {
    return student;
  }

  @PostMapping("/studentInfo")
  public String addStudentInfo(@RequestParam String name, @RequestParam String age) {
    student.put(name, age);
    return "tuika" + name + " " + age + "sai";
  }

}
