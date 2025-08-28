package co.com.pragma.model.application.validation;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class EmailSpecificationTest {

  private final EmailSpecification emailSpecification = new EmailSpecification("email");

  @Test
  void testValidate_validEmail() {
    StepVerifier.create(emailSpecification.validate("test@example.com")).verifyComplete();
  }

  @Test
  void testValidate_nullEmail() {
    StepVerifier.create(emailSpecification.validate(null)).expectError(DomainValidationException.class).verify();
  }

  @Test
  void testValidate_emptyEmail() {
    StepVerifier.create(emailSpecification.validate("")).expectError(DomainValidationException.class).verify();
  }

  @Test
  void testValidate_invalidEmail() {
    StepVerifier.create(emailSpecification.validate("invalid-email")).expectError(DomainValidationException.class).verify();
  }

}
