package co.com.pragma.api.exception.handler;

import co.com.pragma.api.ApplicationWebKeys;
import co.com.pragma.api.exception.ApplicationApiException;
import co.com.pragma.api.exception.AuthenticationApiException;
import co.com.pragma.model.application.exception.ApplicationException;
import co.com.pragma.model.application.validation.DomainValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.HashMap;
import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

  private static final Logger LOG = LoggerFactory.getLogger(GlobalErrorAttributes.class);

  @Override
  public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
    Map<String, Object> errorMap = new HashMap<>();
    Throwable error = getError(request);
    errorMap.put(ApplicationWebKeys.ERROR_ATTRIBUTE_MESSAGE, error.getMessage());
    errorMap.put(ApplicationWebKeys.ERROR_ATTRIBUTE_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
    errorMap.put(ApplicationWebKeys.ERROR_ATTRIBUTE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    errorMap.put(ApplicationWebKeys.ERROR_ATTRIBUTE_PATH, request.path());
    if (error instanceof ApplicationApiException apiException) {
      errorMap.put(ApplicationWebKeys.ERROR_ATTRIBUTE_MESSAGE, apiException.getMessage());
      errorMap.put(ApplicationWebKeys.ERROR_ATTRIBUTE_STATUS, apiException.getStatus().value());
      errorMap.put(ApplicationWebKeys.ERROR_ATTRIBUTE_ERROR, apiException.getStatus().getReasonPhrase());
    }
    if (error instanceof ApplicationException apiException) {
      errorMap.put(ApplicationWebKeys.ERROR_ATTRIBUTE_MESSAGE, apiException.getMessage());
      errorMap.put(ApplicationWebKeys.ERROR_ATTRIBUTE_STATUS, HttpStatus.valueOf(apiException.getStatus()).value());
      errorMap.put(ApplicationWebKeys.ERROR_ATTRIBUTE_ERROR, HttpStatus.valueOf(apiException.getStatus()).getReasonPhrase());
    }
    if (error instanceof DomainValidationException domainException) {
      errorMap.put(ApplicationWebKeys.ERROR_ATTRIBUTE_MESSAGE, domainException.getMessage());
      errorMap.put(ApplicationWebKeys.ERROR_ATTRIBUTE_STATUS, HttpStatus.valueOf(domainException.getStatus()).value());
      errorMap.put("error", HttpStatus.valueOf(domainException.getStatus()).getReasonPhrase());
    }
    if (error instanceof AuthenticationApiException authException) {
      errorMap.put(ApplicationWebKeys.ERROR_ATTRIBUTE_MESSAGE, authException.getMessage());
      errorMap.put(ApplicationWebKeys.ERROR_ATTRIBUTE_STATUS, authException.getStatus().value());
      errorMap.put(ApplicationWebKeys.ERROR_ATTRIBUTE_ERROR, authException.getStatus().getReasonPhrase());
    }
    LOG.error(error.getMessage());
    return errorMap;
  }

}
