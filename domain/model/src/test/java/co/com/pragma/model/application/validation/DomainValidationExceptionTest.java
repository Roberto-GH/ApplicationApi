package co.com.pragma.model.application.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DomainValidationExceptionTest {

    @Test
    void testException() {
        DomainValidationException exception = new DomainValidationException("test message", 400);
        assertEquals("test message", exception.getMessage());
        assertEquals(400, exception.getStatus());
    }
}
