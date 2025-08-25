package co.com.pragma.api.config;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig {

  @Bean
  public WebProperties.Resources resources() {
    return new WebProperties.Resources();
  }

}
