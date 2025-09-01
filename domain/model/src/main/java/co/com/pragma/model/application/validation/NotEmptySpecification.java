package co.com.pragma.model.application.validation;

import co.com.pragma.model.application.constants.ApplicationModelKeys;
import co.com.pragma.model.application.exception.DomainValidationException;
import co.com.pragma.model.application.exception.ErrorEnum;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

public class NotEmptySpecification implements Specification<String> {

  private static final Logger LOG = Logger.getLogger(NotEmptySpecification.class.getName());

  private final String fieldName;

  public NotEmptySpecification(String fieldName) {
    this.fieldName = fieldName;
  }

  @Override
  public Mono<Void> validate(String candidate) {
    if (candidate == null || candidate.trim().isEmpty()) {
      LOG.severe(ApplicationModelKeys.FIELD_NOT_NULL_OR_EMPTY + fieldName);
      return Mono.error(new DomainValidationException(ErrorEnum.INVALID_APPLICATION_DATA, ApplicationModelKeys.FIELD_NOT_NULL_OR_EMPTY + fieldName));
    }
    return Mono.empty();
  }

}
