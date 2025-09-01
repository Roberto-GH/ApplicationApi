package co.com.pragma.model.application.constants;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationModelKeysTest {

    @Test
    void constructorShouldThrowInstantiationException() throws NoSuchMethodException {
        Constructor<ApplicationModelKeys> constructor = ApplicationModelKeys.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);
      assertInstanceOf(InstantiationException.class, exception.getCause());
    }
}
