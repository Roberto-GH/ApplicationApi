package co.com.pragma.r2dbc;

import co.com.pragma.model.application.Application;
import co.com.pragma.model.application.ApplicationData;
import co.com.pragma.model.application.gateways.ApplicationRepository;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class ApplicationReactiveRepositoryAdapter
  extends ReactiveAdapterOperations<Application, ApplicationEntity, UUID, ApplicationReactiveRepository>
  implements ApplicationRepository {

public ApplicationReactiveRepositoryAdapter(ApplicationReactiveRepository repository, ObjectMapper mapper) {
  super(repository, mapper, entity -> mapper.mapBuilder(entity, Application.Builder.class).build());
}

  @Override
  public Mono<Application> saveApplication(Application application) {
    return super.save(application);
  }

  @Override
  public Flux<ApplicationData> findByStatusAndLoanType(Integer status, Integer loanType, Integer pageSize, Integer pageNumber) {
    return repository.findByStatusAndLoanType(status, loanType, pageSize, pageNumber)
      .map(m -> mapper.map(m, ApplicationData.class));
  }

  @Override
  public Mono<Long> countByStatusAndLoanType(Integer status, Integer loanType) {
    return repository.countByStatusAndLoanType(status, loanType);
  }

}
