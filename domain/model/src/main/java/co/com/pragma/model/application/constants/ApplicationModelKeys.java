package co.com.pragma.model.application.constants;

import java.util.regex.Pattern;

public class ApplicationModelKeys {

  private ApplicationModelKeys() throws InstantiationException {
    throw new InstantiationException("Instances are forbidden");
  }

  public static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
  public static final String FIELD_NOT_NULL = "The field cannot be null. => ";
  public static final String FIELD_NOT_EMPTY = "The field cannot be empty. => ";
  public static final String FIELD_NOT_NULL_OR_EMPTY = "The field cannot be null or empty. => ";
  public static final String FIELD_NOT_NULL_OR_ZERO = "The field cannot be null or 0. => ";
  public static final String FIELD_RANGE = "The filed must be between. => ";
  public static final String INVALID_FORMAT = "The format is not a valid of the field. => ";
  public static final String CODE_ERROR_APP_001 = "APP-001";
  public static final String CODE_ERROR_APP_002 = "APP-002";
  public static final String CODE_ERROR_AUTH_001 = "AUTH-001";
  public static final String CODE_ERROR_AUTH_002 = "AUTH-002";
  public static final String CODE_ERROR_AUTH_003 = "AUTH-003";
  public static final String CODE_ERROR_SYS_001 = "SYS-001";
  public static final String CODE_ERROR_RC_001 = "RC-001";
  public static final String DEFAULT_MESSAGE_APP_OO1 = "Application not found.";
  public static final String DEFAULT_MESSAGE_APP_002 = "Invalid application data.";
  public static final String DEFAULT_MESSAGE_AUTH_001 = "Unauthorized access.";
  public static final String DEFAULT_MESSAGE_AUTH_002 = "The provided token is invalid or has expired.";
  public static final String DEFAULT_MESSAGE_AUTH_003 = "Access denied.";
  public static final String DEFAULT_MESSAGE_SYS_001 = "Internal server error.";
  public static final String DEFAULT_MESSAGE_RC_001 = "Internal server conflic.";
  public static final int STATUS_400 = 400;
  public static final int STATUS_401 = 401;
  public static final int STATUS_403 = 403;
  public static final int STATUS_409 = 409;
  public static final int STATUS_500 = 500;

}
