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

        ErrorEnum errorEnum = ErrorEnum.APPLICATION_NOT_FOUND;


        TestException exception = new TestException(errorEnum);


        assertEquals(errorEnum.getDefaultMessage(), exception.getMessage());
        assertEquals(errorEnum.getCode(), exception.getErrorCode());
        assertEquals(errorEnum.getStatus(), exception.getStatus());
    }

    @Test
    void testConstructorWithErrorEnumAndCustomMessage() {

        ErrorEnum errorEnum = ErrorEnum.INVALID_APPLICATION_DATA;
        String customMessage = "This is a custom message";


        TestException exception = new TestException(errorEnum, customMessage);


        assertEquals(customMessage, exception.getMessage());
        assertEquals(errorEnum.getCode(), exception.getErrorCode());
        assertEquals(errorEnum.getStatus(), exception.getStatus());
    }
}
