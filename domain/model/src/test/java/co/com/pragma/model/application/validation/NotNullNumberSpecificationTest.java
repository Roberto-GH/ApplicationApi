package co.com.pragma.model.application.validation;

import co.com.pragma.model.application.exception.DomainValidationException;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class NotNullNumberSpecificationTest {

    private final NotNullNumberSpecification notNullNumberSpecification = new NotNullNumberSpecification("field");

    @Test
    void testValidate_validNumber() {
        StepVerifier.create(notNullNumberSpecification.validate(1L))
                .verifyComplete();
    }

    @Test
    void testValidate_nullNumber() {
        StepVerifier.create(notNullNumberSpecification.validate(null))
                .expectError(DomainValidationException.class)
                .verify();
    }

    @Test
    void testValidate_zeroNumber() {
        StepVerifier.create(notNullNumberSpecification.validate(0L))
                .expectError(DomainValidationException.class)
                .verify();
    }

    @Test
    void testValidate_negativeNumber() {
        StepVerifier.create(notNullNumberSpecification.validate(-1L))
                .expectError(DomainValidationException.class)
                .verify();
    }
}
