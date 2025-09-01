package co.com.pragma.model.application.validation;

import co.com.pragma.model.application.constants.ApplicationModelKeys;
import co.com.pragma.model.application.exception.DomainValidationException;
import co.com.pragma.model.application.exception.ErrorEnum;
import reactor.core.publisher.Mono;

public class EmailSpecification implements Specification<String> {


  private final String fieldName;

  public EmailSpecification(String fieldName) {
    this.fieldName = fieldName;
  }

  @Override
  public Mono<Void> validate(String candidate) {
    if (candidate == null || candidate.trim().isEmpty()) {
      return Mono.error(new DomainValidationException(ErrorEnum.INVALID_APPLICATION_DATA, ApplicationModelKeys.FIELD_NOT_EMPTY + fieldName));
    }
    if (!ApplicationModelKeys.EMAIL_PATTERN.matcher(candidate).matches()) {
      return Mono.error(new DomainValidationException(ErrorEnum.INVALID_APPLICATION_DATA, ApplicationModelKeys.INVALID_FORMAT + fieldName));
    }
    return Mono.empty();
  }

}
