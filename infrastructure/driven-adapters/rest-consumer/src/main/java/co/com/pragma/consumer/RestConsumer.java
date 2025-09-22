package co.com.pragma.consumer;

import co.com.bancolombia.secretsmanager.api.GenericManagerAsync;
import co.com.bancolombia.secretsmanager.api.exceptions.SecretException;
import co.com.pragma.consumer.config.AuthSecretModel;
import co.com.pragma.consumer.constants.RestConsumerKeys;
import co.com.pragma.consumer.exception.RestConsumerException;
import co.com.pragma.model.application.User;
import co.com.pragma.model.application.exception.ErrorEnum;
import co.com.pragma.model.application.gateways.UserRestGateway;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class RestConsumer implements UserRestGateway {

  private static final Logger LOG = LoggerFactory.getLogger(RestConsumer.class);

  private final WebClient client;
  private final DtoMapper dtoMapper;
  private final GenericManagerAsync genericManager;
  private final String authSecretName;

  public RestConsumer(@Qualifier("userApiWebClient") WebClient client, DtoMapper dtoMapper, GenericManagerAsync genericManager, @Value("${adapter.restconsumer.authSecretName}") String authSecretName) {
    this.client = client;
    this.dtoMapper = dtoMapper;
    this.genericManager = genericManager;
    this.authSecretName = authSecretName;
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
    return getAuthSecretModelReactive()
      .flatMap(authSecret -> {
        LoginRequest login = LoginRequest
          .builder()
          .email(authSecret.getEmail())
          .password(authSecret.getPassword())
          .build();
        return client
          .post()
          .uri(RestConsumerKeys.PATH_URI_AUTH)
          .bodyValue(login)
          .retrieve()
          .bodyToMono(TokenResponse.class);
      });
  }

  public Mono<User> fallbackFindUserByEmail(String mail, Exception ex) {
    return Mono.error(new RestConsumerException(ErrorEnum.INTERNAL_CONFLIC_SERVER, RestConsumerKeys.ERROR_FALLBACK_EMAIL + mail + " -- " + ex.getMessage()));
  }

  public Mono<TokenResponse> fallbackGetToken(Exception ex) {
    return Mono.error(new RestConsumerException(ErrorEnum.INTERNAL_CONFLIC_SERVER, RestConsumerKeys.ERROR_FALLBACK_TOKEN + ex.getMessage()));
  }

  private Mono<AuthSecretModel> getAuthSecretModelReactive() {
    try {
      return genericManager
        .getSecret(authSecretName, AuthSecretModel.class)
        .onErrorResume(SecretException.class, e -> Mono.error(new RuntimeException("Error retrieving JWT secret", e)));
    } catch (SecretException e) {
      LOG.error("Synchronous error retrieving secret: {}", e.getMessage());
      return Mono.error(new RuntimeException("Synchronous error retrieving JWT secret", e));
    }
  }

}
