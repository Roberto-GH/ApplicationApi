package co.com.pragma.api.exception;

import co.com.pragma.model.application.exception.CustomException;
import co.com.pragma.model.application.exception.ErrorEnum;
import lombok.Getter;

@Getter
public class ApplicationApiException extends CustomException {

  public ApplicationApiException(ErrorEnum errorEnum) {
    super(errorEnum);
  }

  public ApplicationApiException(ErrorEnum errorEnum, String customMessage) {
    super(errorEnum, customMessage);
  }

}
