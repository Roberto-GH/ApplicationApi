package co.com.pragma.api.exception.handler;

import co.com.pragma.api.exception.ApplicationApiException;
import co.com.pragma.api.exception.AuthenticationApiException;
import co.com.pragma.model.application.exception.ApplicationException;
import co.com.pragma.model.application.validation.DomainValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
class GlobalErrorAttributesTest {

  @Mock
  private ServerRequest request;
  @Mock
  private ErrorAttributeOptions options;

  @InjectMocks
  private GlobalErrorAttributes globalErrorAttributes;

  @Test
  void getErrorAttributes_ApplicationApiException() {
    ApplicationApiException apiException = new ApplicationApiException("Test Exception", HttpStatus.BAD_REQUEST);
    Mockito.when(request.attribute(DefaultErrorAttributes.class.getName() + ".ERROR")).thenReturn(Optional.of(apiException));
    Mockito.when(request.path()).thenReturn("/test/path");
    Map<String, Object> result = globalErrorAttributes.getErrorAttributes(request, options);
    assertAll(
      () -> Assertions.assertEquals("Test Exception", result.get("message")),
      () -> Assertions.assertEquals(400, result.get("status")),
      () -> Assertions.assertEquals("Bad Request", result.get("error")),
      () -> Assertions.assertEquals("/test/path", result.get("path"))
    );
  }

  @Test
  void getErrorAttributes_ApplicationException() {
    ApplicationException apiException = new ApplicationException("Test Exception", 400);
    Mockito.when(request.attribute(DefaultErrorAttributes.class.getName() + ".ERROR")).thenReturn(Optional.of(apiException));
    Mockito.when(request.path()).thenReturn("/test/path");
    Map<String, Object> result = globalErrorAttributes.getErrorAttributes(request, options);
    assertAll(
      () -> Assertions.assertEquals("Test Exception", result.get("message")),
      () -> Assertions.assertEquals(400, result.get("status")),
      () -> Assertions.assertEquals("Bad Request", result.get("error")),
      () -> Assertions.assertEquals("/test/path", result.get("path"))
    );
  }

  @Test
  void getErrorAttributes_DomainValidationException() {
    DomainValidationException apiException = new DomainValidationException("Test Exception", 400);
    Mockito.when(request.attribute(DefaultErrorAttributes.class.getName() + ".ERROR")).thenReturn(Optional.of(apiException));
    Mockito.when(request.path()).thenReturn("/test/path");
    Map<String, Object> result = globalErrorAttributes.getErrorAttributes(request, options);
    assertAll(
      () -> Assertions.assertEquals("Test Exception", result.get("message")),
      () -> Assertions.assertEquals(400, result.get("status")),
      () -> Assertions.assertEquals("Bad Request", result.get("error")),
      () -> Assertions.assertEquals("/test/path", result.get("path"))
    );
  }

  @Test
  void getErrorAttributes_AuthenticationApiException() {
    AuthenticationApiException apiException = new AuthenticationApiException ("Test Exception", HttpStatus.BAD_REQUEST);
    Mockito.when(request.attribute(DefaultErrorAttributes.class.getName() + ".ERROR")).thenReturn(Optional.of(apiException));
    Mockito.when(request.path()).thenReturn("/test/path");
    Map<String, Object> result = globalErrorAttributes.getErrorAttributes(request, options);
    assertAll(
      () -> Assertions.assertEquals("Test Exception", result.get("message")),
      () -> Assertions.assertEquals(400, result.get("status")),
      () -> Assertions.assertEquals("Bad Request", result.get("error")),
      () -> Assertions.assertEquals("/test/path", result.get("path"))
    );
  }

}
