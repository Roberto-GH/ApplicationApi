package co.com.pragma.model.application.validation;

import java.util.logging.Logger;

public class NotEmptySpecification implements Specification<String> {

  private static final Logger LOG = Logger.getLogger(NotEmptySpecification.class.getName());

  private final String fieldName;

  public NotEmptySpecification(String fieldName) {
    this.fieldName = fieldName;
  }

  @Override
  public void validate(String candidate) throws DomainValidationException {
    if (candidate == null || candidate.trim().isEmpty()) {
      LOG.severe("Validation failed: The field '" + fieldName + "' cannot be null or empty.");
      throw new DomainValidationException("The field '" + fieldName + "' cannot be null or empty.", 400);
    }
  }

}
