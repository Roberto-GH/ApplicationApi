package co.com.pragma.r2dbc.constants;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PostgreSQLKeysTest {

    @Test
    void testPrivateConstructor() {
        assertThrows(InvocationTargetException.class, () -> {
            Constructor<PostgreSQLKeys> constructor = PostgreSQLKeys.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });
    }

    @Test
    void testConstantsAreNotNull() {
        assertNotNull(PostgreSQLKeys.API_POSTGRESS_CONNECTION_POOL);
        assertNotNull(PostgreSQLKeys.VALIDATION_QUERY);
        assertNotNull(PostgreSQLKeys.PATH_CONFIG_R2DBC);
        assertNotNull(PostgreSQLKeys.COLUMN_NAME_APPLICATION_ID);
        assertNotNull(PostgreSQLKeys.COLUMN_NAME_IDENTITY);
        assertNotNull(PostgreSQLKeys.COLUMN_NAME_STATUS_ID);
        assertNotNull(PostgreSQLKeys.COLUMN_NAME_LOAN_TYPE_ID);
        assertNotNull(PostgreSQLKeys.COLUMN_NAME_MINIMUN_AMOUNT);
        assertNotNull(PostgreSQLKeys.COLUMN_NAME_MAXIMUN_AMOUNT);
        assertNotNull(PostgreSQLKeys.COLUMN_NAME_INTEREST_RATE);
        assertNotNull(PostgreSQLKeys.COLUMN_NAME_AUTOMATIC_VALIDATION);
    }
}
