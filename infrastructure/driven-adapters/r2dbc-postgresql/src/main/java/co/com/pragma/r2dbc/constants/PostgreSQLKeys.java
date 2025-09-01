package co.com.pragma.r2dbc.constants;

public class PostgreSQLKeys {

  private PostgreSQLKeys() throws InstantiationException {
    throw new InstantiationException("Instances are forbidden");
  }

  public static final String API_POSTGRESS_CONNECTION_POOL = "api-postgres-connection-pool";
  public static final String VALIDATION_QUERY = "SELECT 1";
  public static final int INITIAL_SIZE = 12;
  public static final int MAX_SIZE = 15;
  public static final int MAX_IDLE_TIME = 30;
  public static final String PATH_CONFIG_R2DBC = "adapters.r2dbc";
  public static final String COLUMN_NAME_APPLICATION_ID = "application_id";
  public static final String COLUMN_NAME_IDENTITY = "identity_document";
  public static final String COLUMN_NAME_STATUS_ID = "status_id";
  public static final String COLUMN_NAME_LOAN_TYPE_ID = "loan_type_id";
  public static final String COLUMN_NAME_MINIMUN_AMOUNT = "minimum_amount";
  public static final String COLUMN_NAME_MAXIMUN_AMOUNT = "maximum_amount";
  public static final String COLUMN_NAME_INTEREST_RATE = "interest_rate";
  public static final String COLUMN_NAME_AUTOMATIC_VALIDATION = "automatic_validation";
  public static final String COLUMN_NAME_LOAN_TYPE = "loan_type";
  public static final String COLUMN_NAME_APPLICATION_STATUS = "application_status";
  public static final String INFO_POPULATING_DATA = "Populating ApplicationData with user data => ";
  public static final String INC0MPLETE_DATA = "ApplicationData will be returned incomplete. Error querying user with email => ";

}
