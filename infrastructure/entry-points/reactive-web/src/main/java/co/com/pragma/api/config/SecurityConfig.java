package co.com.pragma.api.config;

import co.com.pragma.api.ApplicationWebKeys;
import co.com.pragma.api.jwt.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableReactiveMethodSecurity
public class SecurityConfig {

  private final SecurityContextRepository securityContextRepository;

  public SecurityConfig(SecurityContextRepository securityContextRepository) {
    this.securityContextRepository = securityContextRepository;
  }

  @Bean
  public SecurityWebFilterChain filterChain(ServerHttpSecurity http, JwtFilter jwtFilter) {
    return http
      .csrf(ServerHttpSecurity.CsrfSpec::disable)
      .authorizeExchange(exchangeSpec -> exchangeSpec
        .pathMatchers(ApplicationWebKeys.ALLOWED_PATHS)
        .permitAll()
        .anyExchange()
        .authenticated())
      .addFilterAfter(jwtFilter, SecurityWebFiltersOrder.FIRST)
      .securityContextRepository(securityContextRepository)
      .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
      .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
      .logout(ServerHttpSecurity.LogoutSpec::disable)
      .build();
  }
}
