package co.com.pragma.model.application.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends Exception {

  private final int status;

  public ApplicationException(String message, int status) {
    super(message);
    this.status = status;
  }

}
