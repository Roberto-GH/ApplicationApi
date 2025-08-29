package co.com.pragma.api;

import co.com.pragma.api.config.ApplicationPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class RouterRestTest {

  @Mock
  private Handler handler;
  @Mock
  private ApplicationPath applicationPath;
  @InjectMocks
  private RouterRest routerRest;
  private WebTestClient webTestClient;

  @BeforeEach
  void setUp() {
    when(applicationPath.getApplication()).thenReturn("/api/v1/application");
    when(handler.listenSaveApplication(any())).thenReturn(ServerResponse.ok().build());
    RouterFunction<ServerResponse> routerFunction = routerRest.routerFunction(handler);
    webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
  }

  @Test
  void testRouterFunction() {
    webTestClient.post().uri("/api/v1/application").exchange().expectStatus().isOk(); // Expecting OK because the handler is mocked to return a successful response
  }

}