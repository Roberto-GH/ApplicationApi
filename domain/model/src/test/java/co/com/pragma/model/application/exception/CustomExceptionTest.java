package co.com.pragma.model.application.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomExceptionTest {

    private static class TestException extends CustomException {
        public TestException(ErrorEnum errorEnum) {
            super(errorEnum);
        }

        public TestException(ErrorEnum errorEnum, String customMessage) {
            super(errorEnum, customMessage);
        }
    }

    @Test
    void testConstructorWithErrorEnum() {
        // Given
        ErrorEnum errorEnum = ErrorEnum.APPLICATION_NOT_FOUND;

        // When
        TestException exception = new TestException(errorEnum);

        // Then
        assertEquals(errorEnum.getDefaultMessage(), exception.getMessage());
        assertEquals(errorEnum.getCode(), exception.getErrorCode());
        assertEquals(errorEnum.getStatus(), exception.getStatus());
    }

    @Test
    void testConstructorWithErrorEnumAndCustomMessage() {
        // Given
        ErrorEnum errorEnum = ErrorEnum.INVALID_APPLICATION_DATA;
        String customMessage = "This is a custom message";

        // When
        TestException exception = new TestException(errorEnum, customMessage);

        // Then
        assertEquals(customMessage, exception.getMessage());
        assertEquals(errorEnum.getCode(), exception.getErrorCode());
        assertEquals(errorEnum.getStatus(), exception.getStatus());
    }
}
