package co.com.pragma.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthenticationApiException extends Exception {

  private final HttpStatus status;

  public AuthenticationApiException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }

}
