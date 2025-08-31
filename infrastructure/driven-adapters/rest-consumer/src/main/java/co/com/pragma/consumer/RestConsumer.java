package co.com.pragma.consumer;

import co.com.pragma.model.application.User;
import co.com.pragma.model.application.gateways.UserRestGateway;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class RestConsumer implements UserRestGateway {

  private final WebClient client;
  private final DtoMapper dtoMapper;
  private final String serviceEmail;
  private final String servicePassword;

  public RestConsumer(@Qualifier("userApiWebClient") WebClient client, DtoMapper dtoMapper, @Value("${adapter.restconsumer.email}") String serviceEmail,
                      @Value("${adapter.restconsumer.password}") String servicePassword) {
    this.client = client;
    this.dtoMapper = dtoMapper;
    this.serviceEmail = serviceEmail;
    this.servicePassword = servicePassword;
  }

  @CircuitBreaker(name = "findUserByEmail", fallbackMethod = "fallbackFindUserByEmail")
  @Override
  public Mono<User> findUserByEmail(String mail) {
    return getToken().flatMap(tokenResponse -> {
      String token = tokenResponse.getToken();
      return client
        .get()
        .uri("/api/v1/user/{email}", mail)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .retrieve()
        .bodyToMono(UserResponseDto.class)
        .map(dtoMapper::toUser);
    });
  }

  @CircuitBreaker(name = "getToken", fallbackMethod = "fallbackGetToken")
  public Mono<TokenResponse> getToken() {
    LoginRequest login = LoginRequest
      .builder()
      .email(serviceEmail)
      .password(servicePassword)
      .build();
    return client
      .post()
      .uri("/auth/v1/login")
      .bodyValue(login)
      .retrieve()
      .bodyToMono(TokenResponse.class);
  }

  public Mono<User> fallbackFindUserByEmail(String mail, Exception ex) {
    return Mono.error(new RuntimeException("Fallback: Error al consultar el usuario por email: " + mail + " -- " + ex.getMessage()));
  }

  public Mono<TokenResponse> fallbackGetToken(Exception ex) {
    return Mono.error(new RuntimeException("Fallback: Error al obtener el token de servicio. " + ex.getMessage()));
  }

}
