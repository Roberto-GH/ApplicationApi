package co.com.pragma.model.application.exception;

public enum ErrorEnum {

  APPLICATION_NOT_FOUND("APP-001", "Application not found.", 404),
  INVALID_APPLICATION_DATA("APP-002", "Invalid application data.", 400),
  UNAUTHORIZED_ACCESS("AUTH-001", "Unauthorized access.", 401),
  INVALID_TOKEN("AUTH-002", "The provided token is invalid or has expired.", 403),
  INTERNAL_SERVER_ERROR("SYS-001", "Internal server error.", 500);

  private final String code;
  private final String defaultMessage;
  private final int status;

  ErrorEnum(String code, String defaultMessage, int status) {
    this.code = code;
    this.defaultMessage = defaultMessage;
    this.status = status;
  }

  public String getCode() {
    return code;
  }

  public String getDefaultMessage() {
    return defaultMessage;
  }

  public int getStatus() {
    return status;
  }

}
