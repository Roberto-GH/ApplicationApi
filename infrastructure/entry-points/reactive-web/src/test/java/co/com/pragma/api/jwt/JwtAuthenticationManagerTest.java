package co.com.pragma.api.jwt;

import co.com.pragma.api.exception.AuthenticationApiException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationManagerTest {

  @Mock
  private Claims claims;
  @Mock
  private JwtProvider jwtProvider;

  @InjectMocks
  private JwtAuthenticationManager jwtAuthenticationManager;

  private UsernamePasswordAuthenticationToken authenticationToken;
  private static final String TOKEN_ADMIN_NOT_EXPIRE = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYW1lIiwicm9sZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfQURNSU4ifV0sImlhdCI6MTc1NjQyNTUwOCwiZXhwIjo0OTEyMTg1NTA4fQ.MvZVTcHhSViiwg52n2assEWIyLCCGfZer5CphJi2Nhs";

  @BeforeEach
  void setUp() {
    authenticationToken = new UsernamePasswordAuthenticationToken(TOKEN_ADMIN_NOT_EXPIRE, TOKEN_ADMIN_NOT_EXPIRE);
  }

  @Test
  void authenticate_shouldReturnAuthenticationForValidToken() {
    List<Map<String, Object>> roles = new ArrayList<>();
    Map<String, Object> roleMap = new HashMap<>();
    roleMap.put("authority", "ROLE_USER");
    roles.add(roleMap);
    when(jwtProvider.getClaims(anyString())).thenReturn(claims);
    when(claims.getSubject()).thenReturn("name");
    when(claims.getOrDefault(Mockito.anyString(), Mockito.any())).thenReturn(roles);
    Mono<Authentication> result = jwtAuthenticationManager.authenticate(authenticationToken);
    StepVerifier.create(result).expectNextMatches(auth -> {
      Assertions.assertAll(
        () -> assertNotNull(auth),
        () -> assertEquals("name", auth.getPrincipal()),
        () -> assertEquals(1, auth.getAuthorities().size()),
        () -> assertEquals(new SimpleGrantedAuthority("ROLE_USER"), auth.getAuthorities().iterator().next())
      );
      return true;
    }).verifyComplete();
  }

  @Test
  void authenticate_shouldThrowAuthenticationApiExceptionForInvalidToken() {
    when(jwtProvider.getClaims(anyString())).thenThrow(new ExpiredJwtException(null, null, "Expired"));
    Mono<Authentication> result = jwtAuthenticationManager.authenticate(authenticationToken);
    StepVerifier.create(result)
      .expectErrorMatches(throwable -> throwable instanceof AuthenticationApiException)
      .verify();
  }

  @Test
  void authenticate_shouldHandleTokenWithoutRoles() {
    when(claims.getSubject()).thenReturn("testUser");
    when(jwtProvider.getClaims(anyString())).thenReturn(claims);
    Mono<Authentication> result = jwtAuthenticationManager.authenticate(authenticationToken);
    StepVerifier.create(result)
      .expectErrorMatches(throwable -> throwable instanceof NullPointerException)
      .verify();
  }

}
