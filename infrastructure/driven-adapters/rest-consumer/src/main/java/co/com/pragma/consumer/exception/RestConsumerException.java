package co.com.pragma.consumer.exception;

import co.com.pragma.model.application.exception.CustomException;
import co.com.pragma.model.application.exception.ErrorEnum;

public class RestConsumerException extends CustomException {

  public RestConsumerException(ErrorEnum errorEnum) {
    super(errorEnum);
  }

  public RestConsumerException(ErrorEnum errorEnum, String customMessage) {
    super(errorEnum, customMessage);
  }

}
