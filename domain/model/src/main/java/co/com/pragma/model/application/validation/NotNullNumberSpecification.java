package co.com.pragma.model.application.validation;

import java.util.logging.Logger;

public class NotNullNumberSpecification implements Specification<Long> {


  private static final Logger LOG = Logger.getLogger(NotEmptySpecification.class.getName());

  private final String fieldName;

  public NotNullNumberSpecification(String fieldName) {
    this.fieldName = fieldName;
  }

  @Override
  public void validate(Long candidate) throws DomainValidationException {
    if (candidate == null || candidate <= 0) {
      LOG.severe("Validation failed: The field '" + fieldName + "' cannot be null or 0.");
      throw new DomainValidationException("The field '" + fieldName + "' cannot be null or 0.", 400);
    }
  }

}
