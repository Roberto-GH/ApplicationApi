package co.com.pragma.api.jwt;

import co.com.pragma.api.constants.ApplicationWebKeys;
import co.com.pragma.api.exception.AuthenticationApiException;
import co.com.pragma.model.application.exception.ErrorEnum;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

  private final JwtProvider jwtProvider;

  public JwtAuthenticationManager(JwtProvider jwtProvider) {
    this.jwtProvider = jwtProvider;
  }

  @Override
  public Mono<Authentication> authenticate(Authentication authentication) {
    return Mono.just(authentication)
      .map(auth -> jwtProvider.getClaims(auth.getCredentials().toString()))
      .onErrorResume(e -> Mono.error(new AuthenticationApiException(ErrorEnum.UNAUTHORIZED_ACCESS, ApplicationWebKeys.JWT_ERROR_BAD_TOKEN)))
      .map(claims -> new UsernamePasswordAuthenticationToken(
        claims.getSubject(),
        null,
        ((List<?>) claims.getOrDefault(ApplicationWebKeys.JWT_ROLES, List.of())).stream()
          .filter(Map.class::isInstance)
          .map(Map.class::cast)
          .map(roleMap -> roleMap.get(ApplicationWebKeys.JWT_AUTHORITY))
          .filter(String.class::isInstance)
          .map(String.class::cast)
          .map(SimpleGrantedAuthority::new)
          .toList())
      );
  }

}
