package co.com.pragma.usecase.application;

import co.com.pragma.model.application.Application;
import co.com.pragma.model.application.gateways.ApplicationRepository;
import co.com.pragma.model.application.validation.AmountInRangeSpecification;
import co.com.pragma.model.application.validation.EmailSpecification;
import co.com.pragma.model.application.validation.NotEmptySpecification;
import co.com.pragma.model.application.validation.Specification;
import co.com.pragma.usecase.application.adapters.ApplicationControllerUseCase;
import reactor.core.publisher.Mono;

public class ApplicationUseCase implements ApplicationControllerUseCase {

  private static final Specification<String> FIRST_NAME_NOT_EMPTY = new NotEmptySpecification("firstName");
  private static final Specification<String> LAST_NAME_NOT_EMPTY = new NotEmptySpecification("lastName");
  private static final Specification<String> EMAIL_FORMAT = new EmailSpecification("email");
  private static final Specification<String> PASSWORD_NOT_EMPTY = new NotEmptySpecification("password");
  private static final Specification<Long> SALARY_RANGE = new AmountInRangeSpecification("baseSalary", 0L, 15000000L);

  private final ApplicationRepository applicationRepository;

  public ApplicationUseCase(ApplicationRepository applicationRepository) {
    this.applicationRepository = applicationRepository;
  }

  @Override
  public Mono<Application> saveApplication(Application application) {
    return applicationRepository.saveApplication(application);
  }

}
