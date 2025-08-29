package co.com.pragma.api.exception.handler;

import co.com.pragma.api.ApplicationWebKeys;
import co.com.pragma.model.application.exception.CustomException;
import co.com.pragma.model.application.exception.ErrorEnum;
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
    errorMap.put(ApplicationWebKeys.ERROR_ATTRIBUTE_MESSAGE, ErrorEnum.INTERNAL_SERVER_ERROR.getDefaultMessage());
    errorMap.put(ApplicationWebKeys.ERROR_ATTRIBUTE_ERROR_CODE, ErrorEnum.INTERNAL_SERVER_ERROR.getCode());
    errorMap.put(ApplicationWebKeys.ERROR_ATTRIBUTE_ERROR, HttpStatus.valueOf(ErrorEnum.INTERNAL_SERVER_ERROR.getStatus()));
    errorMap.put(ApplicationWebKeys.ERROR_ATTRIBUTE_PATH, request.path());
    if (error instanceof CustomException apiException) {
      errorMap.put(ApplicationWebKeys.ERROR_ATTRIBUTE_MESSAGE, apiException.getMessage());
      errorMap.put(ApplicationWebKeys.ERROR_ATTRIBUTE_ERROR_CODE, apiException.getErrorCode());
      errorMap.put(ApplicationWebKeys.ERROR_ATTRIBUTE_ERROR, HttpStatus.valueOf(apiException.getStatus()).getReasonPhrase());
      errorMap.put(ApplicationWebKeys.ERROR_ATTRIBUTE_PATH, request.path());
    }
    LOG.error(error.getMessage());
    return errorMap;
  }

}
