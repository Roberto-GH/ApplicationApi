package co.com.pragma.r2dbc;

import co.com.pragma.model.application.Application;
import co.com.pragma.model.application.ApplicationData;
import co.com.pragma.model.application.gateways.ApplicationRepository;
import co.com.pragma.model.application.gateways.UserRestGateway;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class ApplicationReactiveRepositoryAdapter
  extends ReactiveAdapterOperations<Application, ApplicationEntity, UUID, ApplicationReactiveRepository>
  implements ApplicationRepository {

  private static final Logger log = LoggerFactory.getLogger(ApplicationReactiveRepositoryAdapter.class);
  private final UserRestGateway userRestGateway;

  public ApplicationReactiveRepositoryAdapter(ApplicationReactiveRepository repository, ObjectMapper mapper, UserRestGateway userRestGateway) {
    super(repository, mapper, entity -> mapper.mapBuilder(entity, Application.Builder.class).build());
    this.userRestGateway = userRestGateway;
  }

  @Override
  public Mono<Application> saveApplication(Application application) {
    return super.save(application);
  }

  @Override
  public Flux<ApplicationData> findByStatusAndLoanType(Integer status, Integer loanType, Integer pageSize, Integer pageNumber) {
    return repository.findByStatusAndLoanType(status, loanType, pageSize, pageNumber)
      .map(m -> mapper.map(m, ApplicationData.class))
      .flatMap(this::completeWithUserData);
  }

  private Mono<ApplicationData> completeWithUserData(ApplicationData appData) {
    return userRestGateway.findUserByEmail(appData.getEmail())
      .map(user -> {
          log.info("Completando ApplicationData con datos del usuario: {}", user.getEmail());
          appData.setName(user.getFirstName());
          appData.setBaseSalary(user.getBaseSalary());
          return appData;
      })
      .defaultIfEmpty(appData)
      .onErrorResume(error -> {
          log.error("Error al consultar usuario con email: {}. Se retornará ApplicationData sin completar.", appData.getEmail(), error);
          return Mono.just(appData);
      });
  }

  @Override
  public Mono<Long> countByStatusAndLoanType(Integer status, Integer loanType) {
    return repository.countByStatusAndLoanType(status, loanType);
  }

}
