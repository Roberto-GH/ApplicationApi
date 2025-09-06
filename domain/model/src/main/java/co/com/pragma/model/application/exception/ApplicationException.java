package co.com.pragma.model.application.exception;

public class ApplicationException extends CustomException {

  public ApplicationException(ErrorEnum errorEnum) {
    super(errorEnum);
  }

  public ApplicationException(ErrorEnum errorEnum, String customMessage) {
    super(errorEnum, customMessage);
  }

}
