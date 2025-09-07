package co.com.pragma.usecase.application;

import co.com.pragma.model.application.Application;
import co.com.pragma.model.application.ApplicationData;
import co.com.pragma.model.application.ApplicationList;
import co.com.pragma.model.application.SqsMessageBody;
import co.com.pragma.model.application.exception.DomainValidationException;
import co.com.pragma.model.application.exception.ErrorEnum;
import co.com.pragma.model.application.gateways.ApplicationRepository;
import co.com.pragma.model.application.gateways.LoanTypeRepository;
import co.com.pragma.model.application.gateways.SQSSenderGateway;
import co.com.pragma.model.application.gateways.StatusRepository;
import co.com.pragma.model.application.validation.*;
import co.com.pragma.usecase.application.adapters.ApplicationControllerUseCase;
import co.com.pragma.usecase.application.constants.ApplicationUseCaseKeys;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.logging.Logger;

public class ApplicationUseCase implements ApplicationControllerUseCase {

  private static final Logger LOG = Logger.getLogger(ApplicationUseCase.class.getName());

  private static final Specification<Integer> TERM_NOT_EMPTY = new NotNullIntegerSpecification(ApplicationUseCaseKeys.TERM_FIELD);
  private static final Specification<String> EMAIL_FORMAT = new EmailSpecification(ApplicationUseCaseKeys.EMAIL_FIELD);
  private static final Specification<Long> DOCUMENT_VALIDATE = new NotNullNumberSpecification(ApplicationUseCaseKeys.DOCUMENT_FIELD);
  private static final Specification<BigDecimal> AMOUNT_VALIDATE = new AmountValidationSpecification(ApplicationUseCaseKeys.AMOUNT_FIELD);

  private final ApplicationRepository applicationRepository;
  private final LoanTypeRepository loanTypeRepository;
  private final StatusRepository statusRepository;
  private final SQSSenderGateway senderGateway;

  public ApplicationUseCase(ApplicationRepository applicationRepository, LoanTypeRepository loanTypeRepository, StatusRepository statusRepository,
                            SQSSenderGateway senderGateway) {
    this.applicationRepository = applicationRepository;
    this.loanTypeRepository = loanTypeRepository;
    this.statusRepository = statusRepository;
    this.senderGateway = senderGateway;
  }

  @Override
  public Mono<Application> saveApplication(Application application) {
    return TERM_NOT_EMPTY
      .validate(application.getTerm())
      .then(EMAIL_FORMAT.validate(application.getEmail()))
      .then(DOCUMENT_VALIDATE.validate(application.getIdentityDocument()))
      .then(AMOUNT_VALIDATE.validate(application.getAmount()))
      .then(loanTypeRepository.findById(application.getLoanTypeId()))
      .switchIfEmpty(Mono.error(new DomainValidationException(ErrorEnum.INVALID_APPLICATION_DATA, ApplicationUseCaseKeys.LOAN_ID_NOT_EXIST + application.getLoanTypeId())))
      .then(Mono.just(application))
      .doOnNext(validatedApp -> LOG.info(ApplicationUseCaseKeys.APPLICATION_VALIDATED_SUCCESSFULLY + validatedApp.getEmail()))
      .flatMap(applicationRepository::saveApplication);
  }

  @Override
  public Mono<ApplicationList> getApplicationsByStatusAndLoanType(Integer status, Integer loanType, Integer pageSize, Integer pageNumber) {
    Mono<Void> validationMono = Mono.empty();
    if (status != null) {
      validationMono = validationMono.then(statusRepository.findById(Long.valueOf(status))
        .switchIfEmpty(Mono.error(new DomainValidationException(ErrorEnum.INVALID_APPLICATION_DATA, ApplicationUseCaseKeys.STATUS_ID_NOT_EXIST + status)))
        .then());
    }
    if (loanType != null) {
      validationMono = validationMono.then(loanTypeRepository.findById(Long.valueOf(loanType))
        .switchIfEmpty(Mono.error(new DomainValidationException(ErrorEnum.INVALID_APPLICATION_DATA, ApplicationUseCaseKeys.LOAN_ID_NOT_EXIST + loanType)))
        .then());
    }
    return validationMono.then(applicationRepository.countByStatusAndLoanType(status, loanType)
      .flatMap(totalRecords -> {
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        return applicationRepository.findByStatusAndLoanType(status, loanType, pageSize, pageNumber)
          .map(this::calculateTotalMonthlyPayment)
          .collectList()
          .map(applications -> ApplicationList
            .builder()
            .pageNumber(pageNumber)
            .pageSize(pageSize)
            .totalRecords(totalRecords.intValue())
            .totalPages(totalPages)
            .data(applications)
            .build());
      }));
  }

  @Override
  public Mono<Application> getApplicationById(String id) {
    return applicationRepository.findById(id);
  }

  @Override
  public Mono<Application> patchApplicationStatus(Application application) {
    return applicationRepository.saveApplication(application)
      .onErrorResume(e -> Mono.error(new DomainValidationException(ErrorEnum.INVALID_APPLICATION_DATA, ApplicationUseCaseKeys.ERROR_UPDATE)))
      .flatMap(appl -> {
        if (application.getStatusId() == 2 || application.getStatusId() == 3) {
          return statusRepository.findById(application.getStatusId())
            .switchIfEmpty(Mono.error(new DomainValidationException(ErrorEnum.INVALID_APPLICATION_DATA, ApplicationUseCaseKeys.STATUS_ID_NOT_EXIST + application.getStatusId())))
            .flatMap(status -> {
              SqsMessageBody body = SqsMessageBody
                .builder()
                .subject(ApplicationUseCaseKeys.SUBJECT_UPDATE_SQS_SES)
                .email(application.getEmail())
                .message(ApplicationUseCaseKeys.MESSAGE_UPDATE_SQS_SES + status.getName())
                .build();
              return senderGateway.send(body)
                .onErrorResume(e -> Mono.error(new DomainValidationException(ErrorEnum.INTERNAL_CONFLIC_SERVER)))
                .map(send -> appl);
            });
        } else {
          return Mono.just(appl);
        }
      });
  }

  private ApplicationData calculateTotalMonthlyPayment(ApplicationData appData) {
    LOG.info(ApplicationUseCaseKeys.APPLICATION_STATUS + appData.getStatusId() + " - " + appData.getApplicationStatus());
    if(!Objects.equals(appData.getStatusId(), ApplicationUseCaseKeys.APPROVED_ID)) {
      return appData;
    }
    BigDecimal principal = appData.getAmount();
    BigDecimal annualInterestRate = appData.getInterestRate();
    Integer termInMonths = appData.getTerm();
    if (principal == null || annualInterestRate == null || termInMonths == null || termInMonths <= 0 || annualInterestRate.compareTo(BigDecimal.ZERO) < 0) {
        appData.setTotalMonthlyPayment(BigDecimal.ZERO);
        return appData;
    }
    // Avoid division by zero if interest rate is 0
    if (annualInterestRate.compareTo(BigDecimal.ZERO) == 0) {
      BigDecimal calculatedPayment = principal.divide(BigDecimal.valueOf(termInMonths), 2, RoundingMode.HALF_UP);
      appData.setTotalMonthlyPayment(calculatedPayment);
      return appData;
    }
    // Convert annual percentage rate to monthly decimal rate
    BigDecimal monthlyRate = annualInterestRate.divide(new BigDecimal(ApplicationUseCaseKeys.STRING_100), MathContext.DECIMAL128)
                                               .divide(new BigDecimal(ApplicationUseCaseKeys.STRING_12), MathContext.DECIMAL128);
    //Fórmula de Amortización (Fórmula de la Anualidad) => M = P * [r(1+r)^n] / [(1+r)^n – 1]
    BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);//(1 + r)
    BigDecimal onePlusRToTheN = onePlusR.pow(termInMonths, MathContext.DECIMAL128);//(1 + r)^n
    BigDecimal denominator = onePlusRToTheN.subtract(BigDecimal.ONE);//[(1+r)^n – 1]
    BigDecimal numerator = monthlyRate.multiply(onePlusRToTheN);//r(1+r)^n
    //P *(numerador / denominador)
    BigDecimal monthlyPayment = principal.multiply(numerator).divide(denominator, 2, RoundingMode.HALF_UP);
    appData.setTotalMonthlyPayment(monthlyPayment);
    return appData;
  }
}
