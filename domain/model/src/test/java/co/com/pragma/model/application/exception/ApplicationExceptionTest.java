package co.com.pragma.model.application.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationExceptionTest {

  private static final String mensaje = "Campo invalido";
  private static final int codigo = 400;

  @Test
  void testApplicationExceptionWithMessageAndCode() {
    ApplicationException exception = new ApplicationException(mensaje, codigo);
    assertEquals(codigo, exception.getStatus());
    Assertions.assertAll(
      () -> assertEquals(codigo, exception.getStatus()),
      () -> assertEquals(mensaje, exception.getMessage())
    );
  }

}