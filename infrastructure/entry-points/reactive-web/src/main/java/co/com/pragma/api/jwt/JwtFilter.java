package co.com.pragma.api.jwt;

import co.com.pragma.api.exception.AuthenticationApiException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

@Component
public class JwtFilter implements WebFilter {

  @Override
  @NonNull
  public Mono<Void> filter(ServerWebExchange exchange, @NonNull WebFilterChain chain) {
    ServerHttpRequest request = exchange.getRequest();
    String path = request.getPath().value();
    if(path.contains("auth") || path.contains("swagger") || path.contains("v3/api-docs") || path.contains("webjars"))
      return chain.filter(exchange);
    String auth = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    if(auth == null)
      return Mono.error(new AuthenticationApiException("No token was found", HttpStatus.UNAUTHORIZED));
    if(!auth.startsWith("Bearer "))
      return Mono.error(new AuthenticationApiException("Invalid auth", HttpStatus.UNAUTHORIZED));
    String token = auth.replace("Bearer ", "");
    exchange.getAttributes().put("token", token);
    return chain.filter(exchange);
  }

}
