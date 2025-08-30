package co.com.pragma.model.application.validation;

import co.com.pragma.model.application.exception.DomainValidationException;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

class AmountValidationSpecificationTest {

  private final AmountValidationSpecification amountValidationSpecification = new AmountValidationSpecification("amount");

  @Test
  void testValidate_validAmount() {
    StepVerifier.create(amountValidationSpecification.validate(BigDecimal.TEN)).verifyComplete();
  }

  @Test
  void testValidate_nullAmount() {
    StepVerifier.create(amountValidationSpecification.validate(null)).expectError(DomainValidationException.class).verify();
  }

  @Test
  void testValidate_zeroAmount() {
    StepVerifier.create(amountValidationSpecification.validate(BigDecimal.ZERO)).expectError(DomainValidationException.class).verify();
  }

  @Test
  void testValidate_negativeAmount() {
    StepVerifier.create(amountValidationSpecification.validate(BigDecimal.valueOf(-1))).expectError(DomainValidationException.class).verify();
  }

}
