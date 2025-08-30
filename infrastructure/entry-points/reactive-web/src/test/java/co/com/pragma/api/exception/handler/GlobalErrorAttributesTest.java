package co.com.pragma.api.exception.handler;

import co.com.pragma.api.exception.ApplicationApiException;
import co.com.pragma.api.exception.AuthenticationApiException;
import co.com.pragma.model.application.exception.ApplicationException;
import co.com.pragma.model.application.exception.ErrorEnum;
import co.com.pragma.model.application.exception.DomainValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
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
    ApplicationApiException apiException = new ApplicationApiException(ErrorEnum.INVALID_APPLICATION_DATA, "Test Exception");
    Mockito.when(request.attribute(DefaultErrorAttributes.class.getName() + ".ERROR")).thenReturn(Optional.of(apiException));
    Mockito.when(request.path()).thenReturn("/test/path");
    Map<String, Object> result = globalErrorAttributes.getErrorAttributes(request, options);
    assertAll(
      () -> Assertions.assertEquals("Test Exception", result.get("message")),
      () -> Assertions.assertEquals("APP-002", result.get("error_code")),
      () -> Assertions.assertEquals("Bad Request", result.get("error")),
      () -> Assertions.assertEquals("/test/path", result.get("path"))
    );
  }

  @Test
  void getErrorAttributes_ApplicationException() {
    ApplicationException apiException = new ApplicationException(ErrorEnum.INVALID_APPLICATION_DATA,  "Test Exception");
    Mockito.when(request.attribute(DefaultErrorAttributes.class.getName() + ".ERROR")).thenReturn(Optional.of(apiException));
    Mockito.when(request.path()).thenReturn("/test/path");
    Map<String, Object> result = globalErrorAttributes.getErrorAttributes(request, options);
    assertAll(
      () -> Assertions.assertEquals("Test Exception", result.get("message")),
      () -> Assertions.assertEquals("APP-002", result.get("error_code")),
      () -> Assertions.assertEquals("Bad Request", result.get("error")),
      () -> Assertions.assertEquals("/test/path", result.get("path"))
    );
  }

  @Test
  void getErrorAttributes_DomainValidationException() {
    DomainValidationException apiException = new DomainValidationException(ErrorEnum.INVALID_APPLICATION_DATA, "Test Exception");
    Mockito.when(request.attribute(DefaultErrorAttributes.class.getName() + ".ERROR")).thenReturn(Optional.of(apiException));
    Mockito.when(request.path()).thenReturn("/test/path");
    Map<String, Object> result = globalErrorAttributes.getErrorAttributes(request, options);
    assertAll(
      () -> Assertions.assertEquals("Test Exception", result.get("message")),
      () -> Assertions.assertEquals("APP-002", result.get("error_code")),
      () -> Assertions.assertEquals("Bad Request", result.get("error")),
      () -> Assertions.assertEquals("/test/path", result.get("path"))
    );
  }

  @Test
  void getErrorAttributes_AuthenticationApiException() {
    AuthenticationApiException apiException = new AuthenticationApiException (ErrorEnum.INVALID_APPLICATION_DATA, "Test Exception");
    Mockito.when(request.attribute(DefaultErrorAttributes.class.getName() + ".ERROR")).thenReturn(Optional.of(apiException));
    Mockito.when(request.path()).thenReturn("/test/path");
    Map<String, Object> result = globalErrorAttributes.getErrorAttributes(request, options);
    assertAll(
      () -> Assertions.assertEquals("Test Exception", result.get("message")),
      () -> Assertions.assertEquals("APP-002", result.get("error_code")),
      () -> Assertions.assertEquals("Bad Request", result.get("error")),
      () -> Assertions.assertEquals("/test/path", result.get("path"))
    );
  }

}
