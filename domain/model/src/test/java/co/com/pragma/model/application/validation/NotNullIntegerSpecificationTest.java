package co.com.pragma.model.application.validation;

import co.com.pragma.model.application.exception.DomainValidationException;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class NotNullIntegerSpecificationTest {

    private static final String FIELD_NAME = "testField";

    @Test
    void validateShouldReturnErrorForNullCandidate() {
        NotNullIntegerSpecification specification = new NotNullIntegerSpecification(FIELD_NAME);
        StepVerifier.create(specification.validate(null))
                .expectError(DomainValidationException.class)
                .verify();
    }

    @Test
    void validateShouldReturnErrorForZeroCandidate() {
        NotNullIntegerSpecification specification = new NotNullIntegerSpecification(FIELD_NAME);
        StepVerifier.create(specification.validate(0))
                .expectError(DomainValidationException.class)
                .verify();
    }

    @Test
    void validateShouldReturnErrorForNegativeCandidate() {
        NotNullIntegerSpecification specification = new NotNullIntegerSpecification(FIELD_NAME);
        StepVerifier.create(specification.validate(-5))
                .expectError(DomainValidationException.class)
                .verify();
    }

    @Test
    void validateShouldReturnEmptyMonoForPositiveCandidate() {
        NotNullIntegerSpecification specification = new NotNullIntegerSpecification(FIELD_NAME);
        StepVerifier.create(specification.validate(10))
                .expectComplete()
                .verify();
    }
}
