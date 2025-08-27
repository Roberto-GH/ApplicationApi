package co.com.pragma.usecase.application;

import co.com.pragma.model.application.Application;
import co.com.pragma.model.application.LoanType;
import co.com.pragma.model.application.gateways.ApplicationRepository;
import co.com.pragma.model.application.gateways.LoanTypeRepository;
import co.com.pragma.model.application.validation.*;
import co.com.pragma.usecase.application.adapters.ApplicationControllerUseCase;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.logging.Logger;

public class ApplicationUseCase implements ApplicationControllerUseCase {

  private static final Logger LOG = Logger.getLogger(ApplicationUseCase.class.getName());

  private static final Specification<String> TERM_NOT_EMPTY = new NotEmptySpecification("term");
  private static final Specification<String> EMAIL_FORMAT = new EmailSpecification("email");
  private static final Specification<Long> DOCUMENT_VALIDATE = new NotNullNumberSpecification("identityDocument");
  private static final Specification<BigDecimal> AMOUNT_VALIDATE = new AmountValidationSpecification("amount");

  private final ApplicationRepository applicationRepository;
  private final LoanTypeRepository loanTypeRepository;

  public ApplicationUseCase(ApplicationRepository applicationRepository, LoanTypeRepository loanTypeRepository) {
    this.applicationRepository = applicationRepository;
    this.loanTypeRepository = loanTypeRepository;
  }

  @Override
  public Mono<Application> saveApplication(Application application) {
    return TERM_NOT_EMPTY
      .validate(application.getTerm())
      .then(EMAIL_FORMAT.validate(application.getEmail()))
      .then(DOCUMENT_VALIDATE.validate(application.getIdentityDocument()))
      .then(AMOUNT_VALIDATE.validate(application.getAmount()))
      .then(loanTypeRepository.findById(application.getLoanTypeId()))
      .switchIfEmpty(Mono.error(new DomainValidationException("El tipo de prestamo con id " + application.getLoanTypeId() + " no existe", 400)))
      .then(Mono.just(application))
      .doOnNext(validatedApp -> LOG.info("Application validated successfully: " + validatedApp.getEmail()))
      .flatMap(applicationRepository::saveApplication);
  }

}
