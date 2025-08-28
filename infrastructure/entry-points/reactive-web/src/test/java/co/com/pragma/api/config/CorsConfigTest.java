package co.com.pragma.api.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.cors.reactive.CorsWebFilter;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CorsConfigTest {

  @Test
  void corsWebFilterBeanShouldBeConfiguredCorrectly() {
    String allowedOrigins = "http://localhost:8080,http://example.com";
    CorsConfig corsConfig = new CorsConfig();
    CorsWebFilter corsWebFilter = corsConfig.corsWebFilter(allowedOrigins);
    assertNotNull(corsWebFilter);
  }

}
