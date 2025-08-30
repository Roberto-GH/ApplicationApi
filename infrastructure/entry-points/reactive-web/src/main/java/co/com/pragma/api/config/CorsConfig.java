package co.com.pragma.api.config;

import co.com.pragma.api.constants.ApplicationWebKeys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

  @Bean
  CorsWebFilter corsWebFilter(@Value("${cors.allowed-origins}") String origins) {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.setAllowedOrigins(List.of(origins.split(",")));
    config.setAllowedMethods(Arrays.asList(ApplicationWebKeys.POST_METHOD, ApplicationWebKeys.GET_METHOD));
    config.setAllowedHeaders(List.of(CorsConfiguration.ALL));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration(ApplicationWebKeys.REGISTER_CORS_PATH, config);
    return new CorsWebFilter(source);
  }

}
