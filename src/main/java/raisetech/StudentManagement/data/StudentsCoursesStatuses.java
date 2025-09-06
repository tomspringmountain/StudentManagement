package raisetech.StudentManagement.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

  public class StudentsCoursesStatuses {

    private Long id;

    private Long studentCourseId;

    private Status status;

    private LocalDateTime updatedAt;

    public void setUpdatedAt() {
      this.updatedAt = LocalDateTime.now();
    }

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  public enum Status {
    仮申込, 本申込, 受講中, 受講終了;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Status fromString(String key) {
      for (Status status : Status.values()) {
        if (status.name().equals(key)) {
          return status;
        }
      }
      throw new IllegalArgumentException("Invalid status: " + key);
    }
  }

}
