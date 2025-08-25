package co.com.pragma.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationApiException extends Exception {

  private final HttpStatus status;

  public ApplicationApiException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }

}
