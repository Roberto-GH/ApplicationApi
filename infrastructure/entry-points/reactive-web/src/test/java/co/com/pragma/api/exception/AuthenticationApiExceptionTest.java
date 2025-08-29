package co.com.pragma.api.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthenticationApiExceptionTest {

  @Test
  void testException() {
    AuthenticationApiException exception = new AuthenticationApiException("test message", HttpStatus.UNAUTHORIZED);
    assertEquals("test message", exception.getMessage());
    assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
  }
}
