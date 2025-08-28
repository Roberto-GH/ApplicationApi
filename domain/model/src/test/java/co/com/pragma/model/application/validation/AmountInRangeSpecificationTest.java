package co.com.pragma.model.application.validation;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class AmountInRangeSpecificationTest {

  private final AmountInRangeSpecification amountInRangeSpecification = new AmountInRangeSpecification("amount", 100L, 1000L);

  @Test
  void testValidate_validAmount() {
    StepVerifier.create(amountInRangeSpecification.validate(500L)).verifyComplete();
  }

  @Test
  void testValidate_nullAmount() {
    StepVerifier.create(amountInRangeSpecification.validate(null)).expectError(DomainValidationException.class).verify();
  }

  @Test
  void testValidate_amountLessThanMin() {
    StepVerifier.create(amountInRangeSpecification.validate(50L)).expectError(DomainValidationException.class).verify();
  }

  @Test
  void testValidate_amountGreaterThanMax() {
    StepVerifier.create(amountInRangeSpecification.validate(1500L)).expectError(DomainValidationException.class).verify();
  }

}
