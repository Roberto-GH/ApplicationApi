package co.com.pragma.model.application.validation;

import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.logging.Logger;

public class AmountValidationSpecification implements Specification<BigDecimal> {

  private static final Logger LOG = Logger.getLogger(NotEmptySpecification.class.getName());
  private final String fieldName;

  public AmountValidationSpecification(String fieldName) {
    this.fieldName = fieldName;
  }

  @Override
  public Mono<Void> validate(BigDecimal candidate) {
    if (candidate == null || candidate.compareTo(BigDecimal.ZERO) <= 0) {
      LOG.severe("Validation failed: The field '" + fieldName + "' cannot be null or 0.");
      return Mono.error(new DomainValidationException("The field '" + fieldName + "' cannot be null or 0.", 400));
    }
    return Mono.empty();
  }

}
