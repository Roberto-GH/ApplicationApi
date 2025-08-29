package co.com.pragma.api.config;

import co.com.pragma.api.ApplicationWebKeys;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.util.Arrays;

@Component
public class SecurityHeadersConfig implements WebFilter {

  @Override
  @NonNull
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    HttpHeaders headers = exchange.getResponse().getHeaders();
    Arrays.stream(ApplicationWebKeys.HttpHeadersEnum.values())
      .forEach(header -> headers.set(header.getKey(), header.getValue()));
    return chain.filter(exchange);
  }

}
