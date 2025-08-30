package co.com.pragma.model.application.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationExceptionTest {

  private static final String message = "Invalid field";
  private static final int code = 400;

  @Test
  void testApplicationExceptionWithMessageAndCode() {
    ApplicationException exception = new ApplicationException(ErrorEnum.INVALID_APPLICATION_DATA, message);
    assertEquals(code, exception.getStatus());
    Assertions.assertAll(
      () -> assertEquals(code, exception.getStatus()),
      () -> assertEquals(message, exception.getMessage())
    );
  }

}