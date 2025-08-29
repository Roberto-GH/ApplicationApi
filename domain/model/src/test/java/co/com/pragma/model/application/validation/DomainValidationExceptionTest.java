package co.com.pragma.model.application.validation;

import co.com.pragma.model.application.exception.DomainValidationException;
import co.com.pragma.model.application.exception.ErrorEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DomainValidationExceptionTest {

  @Test
  void testException() {
    DomainValidationException exception = new DomainValidationException(ErrorEnum.INVALID_APPLICATION_DATA, "test message");
    assertEquals("test message", exception.getMessage());
    assertEquals(400, exception.getStatus());
  }

}
