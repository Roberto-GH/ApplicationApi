package co.com.pragma.consumer.constants;

public class RestConsumerKeys {

  private RestConsumerKeys() throws InstantiationException {
    throw new InstantiationException("Instances are forbidden");
  }

  public static final String SPRING = "spring";
  public static final String BEARER = "Bearer ";
  public static final String FUNCTION_FIND_USER_BY_EMAIL = "findUserByEmail";
  public static final String FUNCTION_GET_TOKEN = "getToken";
  public static final String FALLBACK_FIND_USER_BY_EMAIL = "fallbackFindUserByEmail";
  public static final String FALLBACK_GET_TOKEN = "fallbackGetToken";
  public static final String PATH_URI_AUTH = "/auth/v1/login";
  public static final String PATH_URI_EMAIL = "/api/v1/user/{email}";
  public static final String ERROR_FALLBACK_EMAIL = "Fallback: Error querying user by email => ";
  public static final String ERROR_FALLBACK_TOKEN = "Fallback: Error getting service token => ";

}
