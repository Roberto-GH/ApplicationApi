package co.com.pragma.api.constants;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ApplicationWebKeysTest {

    @Test
    void testPrivateConstructor() {
        assertThrows(InvocationTargetException.class, () -> {
            Constructor<ApplicationWebKeys> constructor = ApplicationWebKeys.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });
    }

    @Test
    void testConstantsAreNotNull() {
        assertNotNull(ApplicationWebKeys.PATH_ROUTE_APPLICATION);
        assertNotNull(ApplicationWebKeys.POST_METHOD);
        assertNotNull(ApplicationWebKeys.GET_METHOD);
        assertNotNull(ApplicationWebKeys.REGISTER_CORS_PATH);
        assertNotNull(ApplicationWebKeys.OPEN_API_TITLE);
        assertNotNull(ApplicationWebKeys.OPEN_API_VERSION);
        assertNotNull(ApplicationWebKeys.OPEN_API_DESCRIPTION);
        assertNotNull(ApplicationWebKeys.OPEN_API_SECURITY_NAME);
        assertNotNull(ApplicationWebKeys.OPEN_API_SCHEME);
        assertNotNull(ApplicationWebKeys.OPEN_API_BEARER_FORMAT);
        assertNotNull(ApplicationWebKeys.ERROR_ATTRIBUTE_MESSAGE);
        assertNotNull(ApplicationWebKeys.ERROR_ATTRIBUTE_ERROR_CODE);
        assertNotNull(ApplicationWebKeys.ERROR_ATTRIBUTE_ERROR);
        assertNotNull(ApplicationWebKeys.ERROR_ATTRIBUTE_PATH);
        assertNotNull(ApplicationWebKeys.JWT_ERROR_BAD_TOKEN);
        assertNotNull(ApplicationWebKeys.JWT_ROLES);
        assertNotNull(ApplicationWebKeys.JWT_AUTHORITY);
        assertNotNull(ApplicationWebKeys.HEADER_AUTHORIZATION);
        assertNotNull(ApplicationWebKeys.STRING_BLANK);
        assertNotNull(ApplicationWebKeys.TOKEN);
        assertNotNull(ApplicationWebKeys.STRING_AUTH);
        assertNotNull(ApplicationWebKeys.STRING_SWAGGER);
        assertNotNull(ApplicationWebKeys.STRING_DOCS);
        assertNotNull(ApplicationWebKeys.STRING_WEBJARS);
        assertNotNull(ApplicationWebKeys.NO_TOKEN);
        assertNotNull(ApplicationWebKeys.INVALID_TOKEN);
        assertNotNull(ApplicationWebKeys.SPRING);
        assertNotNull(ApplicationWebKeys.BEARER);
        assertNotNull(ApplicationWebKeys.TOKEN_EXPIRED);
        assertNotNull(ApplicationWebKeys.TOKEN_MALFORMED);
        assertNotNull(ApplicationWebKeys.BAD_SIGNATURE);
        assertNotNull(ApplicationWebKeys.ILLEGAL_ARGS);
        assertNotNull(ApplicationWebKeys.ERROR_DATA_REQUIRED);
        assertNotNull(ApplicationWebKeys.EMAIL_NOT_MATCH);
        assertNotNull(ApplicationWebKeys.OPEN_API_APPLICATION_PATH);
        assertNotNull(ApplicationWebKeys.OPEN_API_BEAN_METHOD_SAVE_APPLICATION);
        assertNotNull(ApplicationWebKeys.OPEN_API_OPERATION_ID);
        assertNotNull(ApplicationWebKeys.OPEN_API_RESPONSE_CODE);
        assertNotNull(ApplicationWebKeys.OPEN_API_DESCRIPTION_SUCCESS);
        assertNotNull(ApplicationWebKeys.OPEN_API_MEDIA_TYPE);
        assertNotNull(ApplicationWebKeys.ALLOWED_PATHS);
    }

    @Test
    void testHttpHeadersEnum() {
        assertEquals("Content-Security-Policy", ApplicationWebKeys.HttpHeadersEnum.CSP_HEADER.getKey());
        assertEquals("default-src 'self'; frame-ancestors 'self'; form-action 'self'", ApplicationWebKeys.HttpHeadersEnum.CSP_HEADER.getValue());

        assertEquals("Strict-Transport-Security", ApplicationWebKeys.HttpHeadersEnum.STS_HEADER.getKey());
        assertEquals("max-age=31536000;", ApplicationWebKeys.HttpHeadersEnum.STS_HEADER.getValue());

        assertEquals("X-Content-Type-Options", ApplicationWebKeys.HttpHeadersEnum.XCTO_HEADER.getKey());
        assertEquals("nosniff", ApplicationWebKeys.HttpHeadersEnum.XCTO_HEADER.getValue());

        assertEquals("Server", ApplicationWebKeys.HttpHeadersEnum.SERVER_HEADER.getKey());
        assertEquals("", ApplicationWebKeys.HttpHeadersEnum.SERVER_HEADER.getValue());

        assertEquals("Cache-Control", ApplicationWebKeys.HttpHeadersEnum.CACHE_CONTROL_HEADER.getKey());
        assertEquals("no-store", ApplicationWebKeys.HttpHeadersEnum.CACHE_CONTROL_HEADER.getValue());

        assertEquals("Pragma", ApplicationWebKeys.HttpHeadersEnum.PRAGMA_HEADER.getKey());
        assertEquals("no-cache", ApplicationWebKeys.HttpHeadersEnum.PRAGMA_HEADER.getValue());

        assertEquals("Referrer-Policy", ApplicationWebKeys.HttpHeadersEnum.REFERRER_POLICY_HEADER.getKey());
        assertEquals("strict-origin-when-cross-origin", ApplicationWebKeys.HttpHeadersEnum.REFERRER_POLICY_HEADER.getValue());
    }
}
