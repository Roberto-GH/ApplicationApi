package co.com.pragma.config;

import co.com.pragma.model.application.gateways.ApplicationRepository;
import co.com.pragma.usecase.application.ApplicationUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {

  @Bean
  public ApplicationUseCase applicationUseCase(ApplicationRepository applicationRepository) {
    return new ApplicationUseCase(applicationRepository);
  }

}
