package co.com.pragma.api.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class OpenApiConfigTest {

  @Test
  void openApiConfig() {
    assertDoesNotThrow(OpenApiConfig::new);
  }
}
