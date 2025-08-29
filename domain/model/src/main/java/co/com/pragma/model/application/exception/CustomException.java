package co.com.pragma.model.application.exception;

public abstract class CustomException extends Exception {

  private final String message;
  private final String errorCode;
  private final int status;

  public CustomException(ErrorEnum errorEnum) {
    super(errorEnum.getDefaultMessage());
    this.message = errorEnum.getDefaultMessage();
    this.errorCode = errorEnum.getCode();
    this.status = errorEnum.getStatus();
  }

  public CustomException(ErrorEnum errorEnum, String customMessage) {
    super(customMessage);
    this.message = customMessage;
    this.errorCode = errorEnum.getCode();
    this.status = errorEnum.getStatus();
  }

  @Override
  public String getMessage() {
    return message;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public int getStatus() {
    return status;
  }

}
