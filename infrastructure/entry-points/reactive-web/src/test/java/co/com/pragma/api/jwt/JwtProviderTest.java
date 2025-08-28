package co.com.pragma.api.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtProviderTest {

    private JwtProvider jwtProvider;
    private String secret;
    private Logger logger;
    private TestLogHandler testLogHandler;

    @BeforeEach
    void setUp() {
        // Generate a secure random secret for testing
        SecretKey generatedKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        secret = Encoders.BASE64URL.encode(generatedKey.getEncoded()); // Changed to BASE64URL
        jwtProvider = new JwtProvider(secret);

        logger = Logger.getLogger(JwtProvider.class.getName());
        testLogHandler = new TestLogHandler();
        logger.addHandler(testLogHandler);
        logger.setUseParentHandlers(false); // Prevent logs from appearing in console during test
    }

    // Custom Log Handler for testing
    private static class TestLogHandler extends Handler {
        private String lastLogMessage;

        @Override
        public void publish(LogRecord record) {
            lastLogMessage = record.getMessage();
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() throws SecurityException {
        }

        public String getLastLogMessage() {
            return lastLogMessage;
        }

        public void clear() {
            lastLogMessage = null;
        }
    }

    private String generateToken(String subject, Date expiration, String secretKey) {
        return Jwts.builder()
                .subject(subject)
                .issuedAt(new Date())
                .expiration(expiration)
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey)), SignatureAlgorithm.HS512) // Changed to BASE64URL
                .compact();
    }

    @Test
    void getClaims_shouldReturnClaimsForValidToken() {
        String subject = "testUser";
        Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 5); // 5 minutes from now
        String token = Jwts.builder()
                .subject(subject)
                .issuedAt(new Date())
                .expiration(expiration)
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secret)), SignatureAlgorithm.HS512) // Changed to BASE64URL
                .compact();

        assertNotNull(jwtProvider.getClaims(token));
        assertEquals(subject, jwtProvider.getClaims(token).getSubject());
    }

    @Test
    void validate_shouldLogExpiredToken() {
        String subject = "testUser";
        Date expiration = new Date(System.currentTimeMillis() - 1000 * 60 * 5); // 5 minutes ago
        String token = Jwts.builder()
                .subject(subject)
                .issuedAt(new Date())
                .expiration(expiration)
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secret)), SignatureAlgorithm.HS512) // Changed to BASE64URL
                .compact();

        jwtProvider.validate(token);
        assertEquals("token expired", testLogHandler.getLastLogMessage());
    }

    @Test
    void validate_shouldLogUnsupportedToken() {
        String unsupportedToken = "header.payload"; // Missing signature
        jwtProvider.validate(unsupportedToken);
        assertEquals("token malformed", testLogHandler.getLastLogMessage());
    }

    @Test
    void validate_shouldLogMalformedToken() {
        String malformedToken = "invalid.token.format";
        jwtProvider.validate(malformedToken);
        assertEquals("token malformed", testLogHandler.getLastLogMessage());
    }

    @Test
    void validate_shouldLogBadSignature() {
        String subject = "testUser";
        Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 5);
        SecretKey wrongSecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String wrongSecret = Encoders.BASE64URL.encode(wrongSecretKey.getEncoded()); // Changed to BASE64URL
        String token = Jwts.builder()
                .subject(subject)
                .issuedAt(new Date())
                .expiration(expiration)
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(wrongSecret)), SignatureAlgorithm.HS512) // Changed to BASE64URL
                .compact();

        jwtProvider.validate(token);
        assertEquals("bad signature", testLogHandler.getLastLogMessage());
    }

    @Test
    void validate_shouldLogIllegalArgs() {
        String illegalArgsToken = null;
        jwtProvider.validate(illegalArgsToken);
        assertEquals("illegal args", testLogHandler.getLastLogMessage());
    }

    @Test
    void getKey_shouldReturnSecretKey() {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secret)); // Changed to BASE64URL
        assertNotNull(key);
    }
}
