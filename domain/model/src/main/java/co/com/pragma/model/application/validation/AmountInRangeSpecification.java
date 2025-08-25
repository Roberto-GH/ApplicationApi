package co.com.pragma.model.application.validation;

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
  public void validate(Long candidate) throws DomainValidationException {
    if (candidate == null) {
      throw new DomainValidationException("The amount '" + fieldName + "' cannot be null.", 400);
    }
    if (candidate.compareTo(min) < 0 || candidate.compareTo(max) > 0) {
      throw new DomainValidationException("The amount '" + fieldName + "' must be between " + min + " and " + max + ".", 400);
    }
  }

}
