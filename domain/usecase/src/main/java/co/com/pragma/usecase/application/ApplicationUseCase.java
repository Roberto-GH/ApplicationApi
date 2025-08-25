package co.com.pragma.usecase.application;

import co.com.pragma.model.application.Application;
import co.com.pragma.model.application.gateways.ApplicationRepository;
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

  public ApplicationUseCase(ApplicationRepository applicationRepository) {
    this.applicationRepository = applicationRepository;
  }

  @Override
  public Mono<Application> saveApplication(Application application) {
    return Mono.fromCallable(() -> {
      TERM_NOT_EMPTY.validate(application.getTerm());
      EMAIL_FORMAT.validate(application.getEmail());
      DOCUMENT_VALIDATE.validate(application.getIdentityDocument());
      AMOUNT_VALIDATE.validate(application.getAmount());
      return application;
    }).doOnNext(validatedApplication -> LOG.info("Application validated successfully: " + validatedApplication.getLoanTypeId()))
      .then(applicationRepository.saveApplication(application));
  }

}
