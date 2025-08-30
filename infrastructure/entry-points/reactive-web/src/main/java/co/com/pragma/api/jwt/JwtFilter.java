package co.com.pragma.api.jwt;

import co.com.pragma.api.constants.ApplicationWebKeys;
import co.com.pragma.api.exception.AuthenticationApiException;
import co.com.pragma.model.application.exception.ErrorEnum;
import org.springframework.http.HttpHeaders;
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
    if (path.contains(ApplicationWebKeys.STRING_AUTH) || path.contains(ApplicationWebKeys.STRING_SWAGGER) || path.contains(ApplicationWebKeys.STRING_DOCS) ||
        path.contains(ApplicationWebKeys.STRING_WEBJARS))
      return chain.filter(exchange);
    String auth = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    if(auth == null)
      return Mono.error(new AuthenticationApiException(ErrorEnum.INVALID_TOKEN, ApplicationWebKeys.NO_TOKEN));
    if(!auth.startsWith(ApplicationWebKeys.BEARER))
      return Mono.error(new AuthenticationApiException(ErrorEnum.INVALID_TOKEN, ApplicationWebKeys.INVALID_TOKEN));
    String token = auth.replace(ApplicationWebKeys.BEARER, ApplicationWebKeys.STRING_BLANK);
    exchange.getAttributes().put(ApplicationWebKeys.TOKEN, token);
    return chain.filter(exchange);
  }

}
