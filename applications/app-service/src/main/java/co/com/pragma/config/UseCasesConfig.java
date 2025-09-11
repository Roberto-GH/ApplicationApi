package co.com.pragma.config;

import co.com.pragma.model.application.gateways.*;
import co.com.pragma.usecase.application.ApplicationUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {

  @Bean
  public ApplicationUseCase applicationUseCase(ApplicationRepository applicationRepository, LoanTypeRepository loanTypeRepository, StatusRepository statusRepository,
                                               SenderGateway senderGateway, UserRestGateway userRestGateway) {
    return new ApplicationUseCase(applicationRepository, loanTypeRepository, statusRepository, senderGateway, userRestGateway);
  }

}
