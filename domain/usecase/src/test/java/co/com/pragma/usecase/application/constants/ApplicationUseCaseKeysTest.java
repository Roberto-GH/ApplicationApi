package co.com.pragma.usecase.application.constants;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ApplicationUseCaseKeysTest {

    @Test
    void testPrivateConstructor() {
        assertThrows(InvocationTargetException.class, () -> {
            Constructor<ApplicationUseCaseKeys> constructor = ApplicationUseCaseKeys.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });
    }

    @Test
    void testConstantsAreNotNull() {
        assertNotNull(ApplicationUseCaseKeys.LOAN_ID_NOT_EXIST);
        assertNotNull(ApplicationUseCaseKeys.APPLICATION_VALIDATED_SUCCESSFULLY);
        assertNotNull(ApplicationUseCaseKeys.TERM_FIELD);
        assertNotNull(ApplicationUseCaseKeys.EMAIL_FIELD);
        assertNotNull(ApplicationUseCaseKeys.DOCUMENT_FIELD);
        assertNotNull(ApplicationUseCaseKeys.AMOUNT_FIELD);
    }
}
