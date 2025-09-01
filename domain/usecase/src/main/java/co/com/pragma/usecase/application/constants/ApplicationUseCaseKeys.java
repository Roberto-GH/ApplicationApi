package co.com.pragma.usecase.application.constants;

public class ApplicationUseCaseKeys {

  private ApplicationUseCaseKeys() throws InstantiationException {
    throw new InstantiationException("Instances are forbidden");
  }

  public static final String LOAN_ID_NOT_EXIST = "Loan type with id does not exist ";
  public static final String STATUS_ID_NOT_EXIST = "Status with id does not exist ";
  public static final String APPLICATION_VALIDATED_SUCCESSFULLY = "Application validated successfully ";
  public static final String TERM_FIELD = "term";
  public static final String EMAIL_FIELD = "email";
  public static final String DOCUMENT_FIELD = "identityDocument";
  public static final String AMOUNT_FIELD = "amount";
  public static final String APPLICATION_STATUS = "Application Status ";
  public static final String STRING_100 = "100";
  public static final String STRING_12 = "12";
  public static final Long APPROVED_ID = 2L;

}
