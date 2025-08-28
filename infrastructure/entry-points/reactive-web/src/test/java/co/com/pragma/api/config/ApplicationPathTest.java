package co.com.pragma.api.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ApplicationPathTest {

    @Test
    void testApplicationPath() {
        ApplicationPath applicationPath = new ApplicationPath();
        String path = "/test/path";

        applicationPath.setApplication(path);

        assertEquals(path, applicationPath.getApplication());
    }
}
