package co.com.pragma.api.exception.handler;

import co.com.pragma.api.exception.ApplicationApiException;
import co.com.pragma.model.application.exception.ErrorEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@WebFluxTest
@ContextConfiguration(classes = {
  GlobalExceptionHandler.class,
  TestRouter.class
})
class GlobalExceptionHandlerTest {

  @Autowired
  private ApplicationContext context;

  @MockitoBean
  private WebProperties.Resources resources;


  private WebTestClient webTestClient;

  @BeforeEach
  public void setUp() {
    webTestClient = WebTestClient.bindToApplicationContext(context).build();
  }

  @Test
  void testHandleException() {
    webTestClient
      .get()
      .uri("/test-exception")
      .exchange()
      .expectStatus()
      .isUnauthorized();
  }

}

@TestConfiguration
class TestRouter {

  @Bean
  public RouterFunction<ServerResponse> testRoutes() {
    return RouterFunctions
      .route()
      .GET("/test-exception", request -> Mono.error(new ApplicationApiException(ErrorEnum.INVALID_APPLICATION_DATA, "Test Exception")))
      .build();
  }

}
