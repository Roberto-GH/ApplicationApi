package co.com.pragma.api.exception.handler;

import co.com.pragma.api.constants.ApplicationWebKeys;
import co.com.pragma.model.application.exception.ErrorEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAccessDeniedHandler implements ServerAccessDeniedHandler {

  private final ObjectMapper objectMapper;

  public CustomAccessDeniedHandler(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(HttpStatus.FORBIDDEN);
    response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    ErrorEnum apiException = ErrorEnum.FORBIDDEN_ACCESS;
    Map<String, Object> errorDetails = new HashMap<>();
    errorDetails.put(ApplicationWebKeys.ERROR_ATTRIBUTE_MESSAGE, apiException.getDefaultMessage());
    errorDetails.put(ApplicationWebKeys.ERROR_ATTRIBUTE_ERROR_CODE, apiException.getCode());
    errorDetails.put(ApplicationWebKeys.ERROR_ATTRIBUTE_ERROR, apiException.getStatus());
    errorDetails.put(ApplicationWebKeys.ERROR_ATTRIBUTE_PATH, exchange.getRequest().getPath().value());
    byte[] responseBytes;
    try {
      responseBytes = objectMapper.writeValueAsString(errorDetails).getBytes(StandardCharsets.UTF_8);
    } catch (JsonProcessingException e) {
      responseBytes = ApplicationWebKeys.WRITTING_JSON_ERROR.getBytes(StandardCharsets.UTF_8);
    }
    DataBuffer buffer = response.bufferFactory().wrap(responseBytes);
    return response.writeWith(Mono.just(buffer));
  }

}
