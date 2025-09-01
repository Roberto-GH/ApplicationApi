package co.com.pragma.consumer.exception;

import co.com.pragma.model.application.exception.ErrorEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RestConsumerExceptionTest {

    @Test
    void constructorShouldSetErrorEnum() {
        ErrorEnum testError = ErrorEnum.APPLICATION_NOT_FOUND;
        RestConsumerException exception = new RestConsumerException(testError);
        assertNotNull(exception);
        assertEquals(testError.getCode(), exception.getErrorCode());
        assertEquals(testError.getDefaultMessage(), exception.getMessage());
        assertEquals(testError.getStatus(), exception.getStatus());
    }

    @Test
    void constructorShouldSetErrorEnumAndCustomMessage() {
        ErrorEnum testError = ErrorEnum.INVALID_APPLICATION_DATA;
        String customMessage = "Test custom message";
        RestConsumerException exception = new RestConsumerException(testError, customMessage);
        assertNotNull(exception);
        assertEquals(testError.getCode(), exception.getErrorCode());
        assertEquals(customMessage, exception.getMessage());
        assertEquals(testError.getStatus(), exception.getStatus());
    }
}