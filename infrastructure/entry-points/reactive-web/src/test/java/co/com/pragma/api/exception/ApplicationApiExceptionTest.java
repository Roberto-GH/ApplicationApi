package co.com.pragma.api.exception;

import co.com.pragma.model.application.exception.ErrorEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApplicationApiExceptionTest {

  @Test
  void testException() {
    ApplicationApiException exception = new ApplicationApiException(ErrorEnum.INVALID_APPLICATION_DATA, "test message");
    Assertions.assertAll(
      () -> assertEquals("test message", exception.getMessage()),
      () -> assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatus()),
      () -> assertEquals(ErrorEnum.INVALID_APPLICATION_DATA.getCode(), exception.getErrorCode())
    );
  }

}
