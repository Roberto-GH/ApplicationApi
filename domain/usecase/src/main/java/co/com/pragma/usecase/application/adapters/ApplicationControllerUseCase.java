package co.com.pragma.usecase.application.adapters;

import co.com.pragma.model.application.Application;
import co.com.pragma.model.application.ApplicationList;
import reactor.core.publisher.Mono;

public interface ApplicationControllerUseCase {

  Mono<Application> saveApplication(Application user);

  Mono<ApplicationList> getApplicationsByStatusAndLoanType(Integer status, Integer loanType, Integer pageSize, Integer pageNumber);

}
