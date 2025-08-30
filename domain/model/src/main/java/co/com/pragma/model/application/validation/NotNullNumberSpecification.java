package co.com.pragma.model.application.validation;

import co.com.pragma.model.application.exception.DomainValidationException;
import co.com.pragma.model.application.exception.ErrorEnum;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

public class NotNullNumberSpecification implements Specification<Long> {


  private static final Logger LOG = Logger.getLogger(NotEmptySpecification.class.getName());

  private final String fieldName;

  public NotNullNumberSpecification(String fieldName) {
    this.fieldName = fieldName;
  }

  @Override
  public Mono<Void> validate(Long candidate) {
    if (candidate == null || candidate <= 0) {
      LOG.severe("Validation failed: The field '" + fieldName + "' cannot be null or 0.");
      return Mono.error(new DomainValidationException(ErrorEnum.INVALID_APPLICATION_DATA, "The field '" + fieldName + "' cannot be null or 0."));
    }
    return Mono.empty();
  }

}
