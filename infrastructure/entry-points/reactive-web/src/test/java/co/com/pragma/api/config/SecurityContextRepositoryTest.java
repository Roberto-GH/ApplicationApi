package co.com.pragma.api.config;

import co.com.pragma.api.jwt.JwtAuthenticationManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityContextRepositoryTest {

  @Mock
  private JwtAuthenticationManager jwtAuthenticationManager;
  @Mock
  private ServerWebExchange exchange;

  @InjectMocks
  private SecurityContextRepository securityContextRepository;

  @Test
  void load_shouldReturnSecurityContextWhenTokenIsValid() {
    String token = "valid_token";
    Authentication authentication = mock(Authentication.class);
    when(exchange.getAttribute("token")).thenReturn(token);
    when(jwtAuthenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(Mono.just(authentication));
    Mono<SecurityContext> result = securityContextRepository.load(exchange);
    StepVerifier.create(result).expectNextMatches(securityContext -> {
      assertNotNull(securityContext);
      return securityContext.getAuthentication() == authentication;
    }).verifyComplete();
  }

  @Test
  void load_shouldPropagateErrorWhenAuthenticationFails() {
    String token = "invalid_token";
    RuntimeException authenticationException = new RuntimeException("Authentication failed");
    when(exchange.getAttribute("token")).thenReturn(token);
    when(jwtAuthenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(Mono.error(authenticationException));
    Mono<SecurityContext> result = securityContextRepository.load(exchange);
    StepVerifier.create(result).expectErrorMatches(throwable -> throwable == authenticationException).verify();
  }

  @Test
  void save_shouldReturnEmptyMono() {
    Mono<Void> result = securityContextRepository.save(exchange, new SecurityContextImpl());
    StepVerifier.create(result).expectComplete().verify();
  }

}
