package co.com.pragma.consumer.constants;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RestConsumerKeysTest {

    @Test
    void constructorShouldThrowInstantiationException() throws NoSuchMethodException {
        Constructor<RestConsumerKeys> constructor = RestConsumerKeys.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
            constructor.newInstance();
        });
        assertTrue(exception.getCause() instanceof InstantiationException);
    }
}