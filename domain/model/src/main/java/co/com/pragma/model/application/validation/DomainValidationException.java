package co.com.pragma.model.application.validation;

public class DomainValidationException extends Exception{

  private final int status;

  public DomainValidationException(String message, int status) {
    super(message);
    this.status = status;
  }

  public int getStatus() {
    return status;
  }

}
