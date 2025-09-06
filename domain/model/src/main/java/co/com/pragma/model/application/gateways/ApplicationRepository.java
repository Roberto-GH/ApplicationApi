package co.com.pragma.model.application.gateways;

import co.com.pragma.model.application.Application;
import co.com.pragma.model.application.ApplicationData;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ApplicationRepository {

  Mono<Application> saveApplication(Application application);

  Flux<ApplicationData> findByStatusAndLoanType(Integer status, Integer loanType, Integer pageSize, Integer pageNumber);

  Mono<Long> countByStatusAndLoanType(Integer status, Integer loanType);

  Mono<Application> findById(String id);

}
