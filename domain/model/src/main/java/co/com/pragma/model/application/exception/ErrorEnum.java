package co.com.pragma.model.application.exception;

import co.com.pragma.model.application.constants.ApplicationModelKeys;

public enum ErrorEnum {

  APPLICATION_NOT_FOUND(ApplicationModelKeys.CODE_ERROR_APP_001, ApplicationModelKeys.DEFAULT_MESSAGE_APP_OO1, ApplicationModelKeys.STATUS_400),
  INVALID_APPLICATION_DATA(ApplicationModelKeys.CODE_ERROR_APP_002, ApplicationModelKeys.DEFAULT_MESSAGE_APP_002, ApplicationModelKeys.STATUS_400),
  UNAUTHORIZED_ACCESS(ApplicationModelKeys.CODE_ERROR_AUTH_001, ApplicationModelKeys.DEFAULT_MESSAGE_AUTH_001, ApplicationModelKeys.STATUS_401),
  INVALID_TOKEN(ApplicationModelKeys.CODE_ERROR_AUTH_002, ApplicationModelKeys.DEFAULT_MESSAGE_AUTH_002, ApplicationModelKeys.STATUS_403),
  INTERNAL_SERVER_ERROR(ApplicationModelKeys.CODE_ERROR_SYS_001, ApplicationModelKeys.DEFAULT_MESSAGE_SYS_001, ApplicationModelKeys.STATUS_500),
  INTERNAL_CONFLIC_SERVER(ApplicationModelKeys.CODE_ERROR_RC_001, ApplicationModelKeys.DEFAULT_MESSAGE_RC_001, ApplicationModelKeys.STATUS_409);

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
