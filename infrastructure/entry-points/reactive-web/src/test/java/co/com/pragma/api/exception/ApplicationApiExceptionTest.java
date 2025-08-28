package co.com.pragma.api.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApplicationApiExceptionTest {

    @Test
    void testException() {
        ApplicationApiException exception = new ApplicationApiException("test message", HttpStatus.BAD_REQUEST);
        assertEquals("test message", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }
}
