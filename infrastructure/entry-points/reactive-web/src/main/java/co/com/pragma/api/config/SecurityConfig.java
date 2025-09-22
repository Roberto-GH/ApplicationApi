package co.com.pragma.api.config;

import co.com.pragma.api.constants.ApplicationWebKeys;
import co.com.pragma.api.jwt.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;

@Configuration
@EnableReactiveMethodSecurity
public class SecurityConfig {

  private final SecurityContextRepository securityContextRepository;
  private final ServerAccessDeniedHandler accessDeniedHandler;

  public SecurityConfig(SecurityContextRepository securityContextRepository, ServerAccessDeniedHandler accessDeniedHandler) {
    this.securityContextRepository = securityContextRepository;
    this.accessDeniedHandler = accessDeniedHandler;
  }

  @Bean
  @Order(1)
  public SecurityWebFilterChain actuatorFilterChain(ServerHttpSecurity http) {
    return http
      .securityMatcher(new PathPatternParserServerWebExchangeMatcher(ApplicationWebKeys.ALLOWED_PATH_ACTUATOR))
      .csrf(ServerHttpSecurity.CsrfSpec::disable)
      .authorizeExchange(exchangeSpec -> exchangeSpec
        .anyExchange()
        .permitAll())
      .build();
  }

  @Bean
  @Order(2)
  public SecurityWebFilterChain apiFilterChain(ServerHttpSecurity http, JwtFilter jwtFilter) {
    return http
      .csrf(ServerHttpSecurity.CsrfSpec::disable)
      .authorizeExchange(exchangeSpec -> exchangeSpec
        .pathMatchers(ApplicationWebKeys.ALLOWED_PATHS)
        .permitAll()
        .anyExchange()
        .authenticated())
      .addFilterAfter(jwtFilter, SecurityWebFiltersOrder.FIRST)
      .exceptionHandling(spec -> spec.accessDeniedHandler(accessDeniedHandler))
      .securityContextRepository(securityContextRepository)
      .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
      .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
      .logout(ServerHttpSecurity.LogoutSpec::disable)
      .build();
  }

}
