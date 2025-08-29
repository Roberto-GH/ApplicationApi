package co.com.pragma.api.jwt;

import co.com.pragma.api.exception.AuthenticationApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

  @Mock
  private ServerWebExchange exchange;
  @Mock
  private WebFilterChain chain;
  @Mock
  private ServerHttpRequest request;
  @Mock
  private HttpHeaders headers;
  @Mock
  private RequestPath path;

  @InjectMocks
  private JwtFilter jwtFilter;

  private Map<String, Object> attributes;

  @BeforeEach
  void setUp() {
    attributes = new HashMap<>();
    when(exchange.getRequest()).thenReturn(request);
  }

  @Test
  void filter_shouldExtractAndPutTokenInAttributes() {
    when(exchange.getAttributes()).thenReturn(attributes);
    String tokenValue = "valid.jwt.token";
    when(request.getPath()).thenReturn(path);
    when(path.value()).thenReturn("/api/some-endpoint");
    when(request.getHeaders()).thenReturn(headers);
    when(headers.getFirst(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + tokenValue);
    when(chain.filter(exchange)).thenReturn(Mono.empty());
    StepVerifier.create(jwtFilter.filter(exchange, chain)).verifyComplete();
    assertEquals(tokenValue, exchange.getAttributes().get("token"));
    verify(chain, times(1)).filter(exchange);
  }

  @Test
  void filter_shouldAllowAuth() {
    when(request.getPath()).thenReturn(path);
    when(path.value()).thenReturn("/auth/some-endpoint");
    when(chain.filter(exchange)).thenReturn(Mono.empty());
    StepVerifier.create(jwtFilter.filter(exchange, chain)).verifyComplete();
    verify(chain, times(1)).filter(exchange);
  }

   @Test
   void filter_shouldAllowApiDocs() {
     when(request.getPath()).thenReturn(path);
     when(path.value()).thenReturn("v3/api-docs");
     when(chain.filter(exchange)).thenReturn(Mono.empty());
     StepVerifier.create(jwtFilter.filter(exchange, chain)).verifyComplete();
     verify(chain, times(1)).filter(exchange);
   }
   @Test
   void filter_shouldAllowSwagger() {
     when(request.getPath()).thenReturn(path);
     when(path.value()).thenReturn("/swagger/some-endpoint");
     when(chain.filter(exchange)).thenReturn(Mono.empty());
     StepVerifier.create(jwtFilter.filter(exchange, chain)).verifyComplete();
     verify(chain, times(1)).filter(exchange);
   }

  @Test
  void filter_shouldAllowWebjars() {
    when(request.getPath()).thenReturn(path);
    when(path.value()).thenReturn("/webjars/some-endpoint");
    when(chain.filter(exchange)).thenReturn(Mono.empty());
    StepVerifier.create(jwtFilter.filter(exchange, chain)).verifyComplete();
    verify(chain, times(1)).filter(exchange);
  }

   @Test
   void filter_shouldThrowErrorWhenNoAuthorizationHeader() {
     when(request.getPath()).thenReturn(path);
     when(path.value()).thenReturn("/api/some-endpoint");
     when(request.getHeaders()).thenReturn(headers);
     when(headers.getFirst(HttpHeaders.AUTHORIZATION)).thenReturn(null);
     StepVerifier
       .create(jwtFilter.filter(exchange, chain))
       .expectErrorMatches(throwable -> throwable instanceof AuthenticationApiException)
       .verify();
     verify(chain, never()).filter(exchange);
   }

  @Test
  void filter_shouldThrowErrorWhenInvalidAuthorizationFormat() {
    when(request.getPath()).thenReturn(path);
    when(path.value()).thenReturn("/api/some-endpoint");
    when(request.getHeaders()).thenReturn(headers);
    when(headers.getFirst(HttpHeaders.AUTHORIZATION)).thenReturn("invalid.token.no.valid");
    StepVerifier
      .create(jwtFilter.filter(exchange, chain))
      .expectErrorMatches(throwable -> throwable instanceof AuthenticationApiException)
      .verify();
    verify(chain, never()).filter(exchange);
  }

}
