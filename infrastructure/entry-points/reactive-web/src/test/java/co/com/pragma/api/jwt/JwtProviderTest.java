package co.com.pragma.api.jwt;

import com.google.gson.Gson;
import com.nimbusds.jose.shaded.gson.reflect.TypeToken;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.logging.LogManager;

@ExtendWith(OutputCaptureExtension.class)
class JwtProviderTest {

  private static final String SECRET = "yuakgfuhasdfgiaeufygweuyfgasdgfeufadf67783465radyfg387g83t8dgf";
  private static final String TOKEN_USER_NOT_EXPIRE = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYW1lIiwicm9sZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfVVNFUiJ9XSwiaWF0IjoxNzU2NDI1NDU0LCJleHAiOjQ5MTIxODU0NTR9.kdNrMt2_gr1_dFaUFfyaBMEPR5MmU6jP6iPHA6OzoSM";
  private static final String TOKEN_ADMIN_NOT_EXPIRE = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYW1lIiwicm9sZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfQURNSU4ifV0sImlhdCI6MTc1NjQyNTUwOCwiZXhwIjo0OTEyMTg1NTA4fQ.MvZVTcHhSViiwg52n2assEWIyLCCGfZer5CphJi2Nhs";
  private static final String TOKEN_ADMIN_EXPIRE = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYW1lIiwicm9sZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfQURNSU4ifV0sImlhdCI6MTc1NjQyNTU2NiwiZXhwIjoxNzU2NDI1NTY2fQ.xQ8jlYJJ1leJEnD2F1JMJOXKZmObfb0CJvAAlV0pNXg";
  private static final String WRONG_SECRET_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYW1lIiwicm9sZXMiOiJST0xFX1VTRVIiLCJpYXQiOjE3NTY0MzI5NzcsImV4cCI6MTc1NjQzMjk3N30.VnuKh3sZElgY3vHmlgxOZtNZlxGKaOcUDObhRKjZ5L8";
  private static final String SUBJECT = "name";
  private static final Gson gson = new Gson();

 private JwtProvider jwtProvider;

  @BeforeEach
  void setUp() {
    jwtProvider = new JwtProvider(SECRET);
  }

  @AfterEach
  void reset() throws Exception {
    LogManager.getLogManager().readConfiguration();
  }

  @Test
  public void tokenUserGetClaimsTest() {
    Claims claims = jwtProvider.getClaims(TOKEN_USER_NOT_EXPIRE);
    String authorityString = gson.toJson(claims.getOrDefault("roles", List.of()));
    Type listType = new TypeToken<List<Map<String, String>>>() {}.getType();
    List<Map<String, String>> authorities = gson.fromJson(authorityString, listType);
    Assertions.assertAll(
      () -> Assertions.assertEquals("ROLE_USER", authorities.getFirst().get("authority")),
      () -> Assertions.assertNotNull(claims),
      () -> Assertions.assertEquals(SUBJECT, jwtProvider.getClaims(TOKEN_USER_NOT_EXPIRE).getSubject())
    );
  }

  @Test
  public void tokenAdminGetClaimsTest() {
    Claims claims = jwtProvider.getClaims(TOKEN_ADMIN_NOT_EXPIRE);
    String authorityString = gson.toJson(claims.getOrDefault("roles", List.of()));
    Type listType = new TypeToken<List<Map<String, String>>>() {}.getType();
    List<Map<String, String>> authorities = gson.fromJson(authorityString, listType);
    Assertions.assertAll(
      () -> Assertions.assertEquals("ROLE_ADMIN", authorities.getFirst().get("authority")),
      () -> Assertions.assertNotNull(claims),
      () -> Assertions.assertEquals(SUBJECT, jwtProvider.getClaims(TOKEN_USER_NOT_EXPIRE).getSubject())
    );
  }

  @Test
  void validate_shouldLogExpiredToken(CapturedOutput output) {
    jwtProvider.validate(TOKEN_ADMIN_EXPIRE);
    Assertions.assertTrue(output.getAll().contains("token expired"));
  }

  @Test
  void validate_shouldLogUnsupportedToken(CapturedOutput output) {
    String unsupportedToken = "header.payload";
    jwtProvider.validate(unsupportedToken);
    Assertions.assertTrue(output.getAll().contains("token malformed"));
  }

  @Test
  void validate_shouldLogMalformedToken(CapturedOutput output) {
    String malformedToken = "invalid.token.format";
    jwtProvider.validate(malformedToken);
    Assertions.assertTrue(output.getAll().contains("token malformed"));
  }

  @Test
  void validate_shouldLogBadSignature(CapturedOutput output) {
    jwtProvider.validate(WRONG_SECRET_TOKEN);
    Assertions.assertTrue(output.getAll().contains("bad signature"));
  }

  @Test
  void validate_shouldLogIllegalArgs(CapturedOutput output) {
    jwtProvider.validate(null);
    Assertions.assertTrue(output.getAll().contains("illegal args"));
  }

}
