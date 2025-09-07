package co.com.pragma.config;

import co.com.pragma.model.application.gateways.ApplicationRepository;
import co.com.pragma.model.application.gateways.LoanTypeRepository;
import co.com.pragma.model.application.gateways.SQSSenderGateway;
import co.com.pragma.model.application.gateways.StatusRepository;
import co.com.pragma.usecase.application.ApplicationUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {

  @Bean
  public ApplicationUseCase applicationUseCase(ApplicationRepository applicationRepository, LoanTypeRepository loanTypeRepository, StatusRepository statusRepository,
                                               SQSSenderGateway senderGateway) {
    return new ApplicationUseCase(applicationRepository, loanTypeRepository, statusRepository, senderGateway);
  }

}
