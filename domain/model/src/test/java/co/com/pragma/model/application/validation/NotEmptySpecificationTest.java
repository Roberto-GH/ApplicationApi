package co.com.pragma.model.application.validation;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class NotEmptySpecificationTest {

  private final NotEmptySpecification notEmptySpecification = new NotEmptySpecification("field");

  @Test
  void testValidate_validString() {
    StepVerifier.create(notEmptySpecification.validate("valid")).verifyComplete();
  }

  @Test
  void testValidate_nullString() {
    StepVerifier.create(notEmptySpecification.validate(null)).expectError(DomainValidationException.class).verify();
  }

  @Test
  void testValidate_emptyString() {
    StepVerifier.create(notEmptySpecification.validate("")).expectError(DomainValidationException.class).verify();
  }

  @Test
  void testValidate_blankString() {
    StepVerifier.create(notEmptySpecification.validate("   ")).expectError(DomainValidationException.class).verify();
  }

}
