package raisetech.StudentManagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import raisetech.StudentManagement.exception.ValidException;

//@Validated
//@RestController
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // Bean Validation エラーの共通処理
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<String> handleValidationErrors(MethodArgumentNotValidException ex)
//        throws ValidationException {
//      String errorMessage = ex.getBindingResult().getFieldErrors().stream()
//          .map(error -> error.getDefaultMessage())
//          .collect(Collectors.joining(" / ")); // 複数エラーはスラッシュ区切り
//
//      throw new ValidationException(errorMessage);
//    }

    // 独自例外のハンドリング
    @ExceptionHandler(ValidException.class)
    public ResponseEntity<String> handleValidException(ValidException ex) {
      System.out.println(ex.toString());

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.toString());
    }

    // 他の例外の共通処理（任意）
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleOtherExceptions(Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("予期しないエラーが発生しました。");
    }

  }



