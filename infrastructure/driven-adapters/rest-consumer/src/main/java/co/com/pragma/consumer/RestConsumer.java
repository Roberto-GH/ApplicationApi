package co.com.pragma.consumer;

import co.com.pragma.consumer.constants.RestConsumerKeys;
import co.com.pragma.consumer.exception.RestConsumerException;
import co.com.pragma.model.application.User;
import co.com.pragma.model.application.exception.ErrorEnum;
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

  @CircuitBreaker(name = RestConsumerKeys.FUNCTION_FIND_USER_BY_EMAIL, fallbackMethod = RestConsumerKeys.FALLBACK_FIND_USER_BY_EMAIL)
  @Override
  public Mono<User> findUserByEmail(String mail) {
    return getToken().flatMap(tokenResponse -> {
      String token = tokenResponse.getToken();
      return client
        .get()
        .uri(RestConsumerKeys.PATH_URI_EMAIL, mail)
        .header(HttpHeaders.AUTHORIZATION, RestConsumerKeys.BEARER + token)
        .retrieve()
        .bodyToMono(UserResponseDto.class)
        .map(dtoMapper::toUser);
    });
  }

  @CircuitBreaker(name = RestConsumerKeys.FUNCTION_GET_TOKEN, fallbackMethod = RestConsumerKeys.FALLBACK_GET_TOKEN)
  public Mono<TokenResponse> getToken() {
    LoginRequest login = LoginRequest
      .builder()
      .email(serviceEmail)
      .password(servicePassword)
      .build();
    return client
      .post()
      .uri(RestConsumerKeys.PATH_URI_AUTH)
      .bodyValue(login)
      .retrieve()
      .bodyToMono(TokenResponse.class);
  }

  public Mono<User> fallbackFindUserByEmail(String mail, Exception ex) {
    return Mono.error(new RestConsumerException(ErrorEnum.INTERNAL_CONFLIC_SERVER, RestConsumerKeys.ERROR_FALLBACK_EMAIL + mail + " -- " + ex.getMessage()));
  }

  public Mono<TokenResponse> fallbackGetToken(Exception ex) {
    return Mono.error(new RestConsumerException(ErrorEnum.INTERNAL_CONFLIC_SERVER, RestConsumerKeys.ERROR_FALLBACK_TOKEN + ex.getMessage()));
  }

}
