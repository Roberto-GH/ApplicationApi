package co.com.pragma.model.application.validation;

import co.com.pragma.model.application.constants.ApplicationModelKeys;
import co.com.pragma.model.application.exception.DomainValidationException;
import co.com.pragma.model.application.exception.ErrorEnum;
import reactor.core.publisher.Mono;

public class AmountInRangeSpecification implements Specification<Long> {

  private final Long min;
  private final Long max;
  private final String fieldName;

  public AmountInRangeSpecification(String fieldName, Long min, Long max) {
    this.fieldName = fieldName;
    this.min = min;
    this.max = max;
  }

  @Override
  public Mono<Void> validate(Long candidate) {
    if (candidate == null) {
      return Mono.error(new DomainValidationException(ErrorEnum.INVALID_APPLICATION_DATA, ApplicationModelKeys.FIELD_NOT_NULL + fieldName));
    }
    if (candidate.compareTo(min) < 0 || candidate.compareTo(max) > 0) {
      return Mono.error(new DomainValidationException(ErrorEnum.INVALID_APPLICATION_DATA, ApplicationModelKeys.FIELD_RANGE + min + " - " + max + " - " +  fieldName));
    }
    return Mono.empty();
  }

}
