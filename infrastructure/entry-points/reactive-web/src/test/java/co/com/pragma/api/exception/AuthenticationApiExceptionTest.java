package co.com.pragma.api.exception;

import co.com.pragma.model.application.exception.ErrorEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthenticationApiExceptionTest {

  @Test
  void testException() {
    AuthenticationApiException exception = new AuthenticationApiException(ErrorEnum.UNAUTHORIZED_ACCESS, "test message");
    assertEquals("test message", exception.getMessage());
    assertEquals(ErrorEnum.UNAUTHORIZED_ACCESS.getStatus(), exception.getStatus());
  }
}
