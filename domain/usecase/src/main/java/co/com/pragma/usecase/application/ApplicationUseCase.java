package co.com.pragma.usecase.application;

import co.com.pragma.model.application.Application;
import co.com.pragma.model.application.LoanType;
import co.com.pragma.model.application.gateways.ApplicationRepository;
import co.com.pragma.model.application.gateways.LoanTypeRepository;
import co.com.pragma.model.application.validation.*;
import co.com.pragma.usecase.application.adapters.ApplicationControllerUseCase;
import co.com.pragma.usecase.application.constants.ApplicationUseCaseKeys;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.logging.Logger;

public class ApplicationUseCase implements ApplicationControllerUseCase {

  private static final Logger LOG = Logger.getLogger(ApplicationUseCase.class.getName());

  private static final Specification<String> TERM_NOT_EMPTY = new NotEmptySpecification(ApplicationUseCaseKeys.TERM_FIELD);
  private static final Specification<String> EMAIL_FORMAT = new EmailSpecification(ApplicationUseCaseKeys.EMAIL_FIELD);
  private static final Specification<Long> DOCUMENT_VALIDATE = new NotNullNumberSpecification(ApplicationUseCaseKeys.DOCUMENT_FIELD);
  private static final Specification<BigDecimal> AMOUNT_VALIDATE = new AmountValidationSpecification(ApplicationUseCaseKeys.AMOUNT_FIELD);

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
      .switchIfEmpty(Mono.error(new DomainValidationException(ApplicationUseCaseKeys.LOAD_ID_NO_EXIST + application.getLoanTypeId(), 400)))
      .then(Mono.just(application))
      .doOnNext(validatedApp -> LOG.info(ApplicationUseCaseKeys.APPLICATION_VALIDATED_SUCCESSFULLY + validatedApp.getEmail()))
      .flatMap(applicationRepository::saveApplication);
  }

}
