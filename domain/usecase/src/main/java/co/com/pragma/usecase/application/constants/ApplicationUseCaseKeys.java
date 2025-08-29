package co.com.pragma.usecase.application.constants;

public class ApplicationUseCaseKeys {

  private ApplicationUseCaseKeys() throws InstantiationException {
    throw new InstantiationException("Instances are forbidden");
  }

  public static final String LOAD_ID_NO_EXIST = "No existe el tipo de prestamo con id ";
  public static final String APPLICATION_VALIDATED_SUCCESSFULLY = "Application validated successfully ";
  public static final String TERM_FIELD = "term";
  public static final String EMAIL_FIELD = "email";
  public static final String DOCUMENT_FIELD = "identityDocument";
  public static final String AMOUNT_FIELD = "amount";

}
