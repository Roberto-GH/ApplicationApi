package co.com.pragma.usecase.application;

import co.com.pragma.model.application.*;
import co.com.pragma.model.application.enums.QueueAlias;
import co.com.pragma.model.application.event.ActiveLoan;
import co.com.pragma.model.application.event.NewLoan;
import co.com.pragma.model.application.event.ValidationRequestEvent;
import co.com.pragma.model.application.exception.DomainValidationException;
import co.com.pragma.model.application.exception.ErrorEnum;
import co.com.pragma.model.application.gateways.*;
import co.com.pragma.model.application.validation.AmountValidationSpecification;
import co.com.pragma.model.application.validation.EmailSpecification;
import co.com.pragma.model.application.validation.NotNullIntegerSpecification;
import co.com.pragma.model.application.validation.NotNullNumberSpecification;
import co.com.pragma.model.application.validation.Specification;
import co.com.pragma.usecase.application.adapters.ApplicationControllerUseCase;
import co.com.pragma.usecase.application.constants.ApplicationUseCaseKeys;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ApplicationUseCase implements ApplicationControllerUseCase {

  private static final Logger LOG = Logger.getLogger(ApplicationUseCase.class.getName());

  private static final Specification<Integer> TERM_NOT_EMPTY = new NotNullIntegerSpecification(ApplicationUseCaseKeys.TERM_FIELD);
  private static final Specification<String> EMAIL_FORMAT = new EmailSpecification(ApplicationUseCaseKeys.EMAIL_FIELD);
  private static final Specification<Long> DOCUMENT_VALIDATE = new NotNullNumberSpecification(ApplicationUseCaseKeys.DOCUMENT_FIELD);
  private static final Specification<BigDecimal> AMOUNT_VALIDATE = new AmountValidationSpecification(ApplicationUseCaseKeys.AMOUNT_FIELD);
  private static final BigDecimal PERCENTAGE_DIVISOR = new BigDecimal("100");
  private static final BigDecimal MONTHS_IN_YEAR = new BigDecimal("12");

  private final ApplicationRepository applicationRepository;
  private final LoanTypeRepository loanTypeRepository;
  private final StatusRepository statusRepository;
  private final SenderGateway senderGateway;
  private final UserRestGateway userRestGateway;

  public ApplicationUseCase(ApplicationRepository applicationRepository, LoanTypeRepository loanTypeRepository, StatusRepository statusRepository,
                            SenderGateway senderGateway, UserRestGateway userRestGateway) {
    this.applicationRepository = applicationRepository;
    this.loanTypeRepository = loanTypeRepository;
    this.statusRepository = statusRepository;
    this.senderGateway = senderGateway;
    this.userRestGateway = userRestGateway;
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
      .flatMap(applicationRepository::saveApplication)
      .flatMap(this::sendValidationRequest);
  }

  private Mono<Application> sendValidationRequest(Application savedApplication) {
    Mono<User> userMono = userRestGateway.findUserByEmail(savedApplication.getEmail());
    Mono<LoanType> loanTypeMono = loanTypeRepository.findById(savedApplication.getLoanTypeId());
    Flux<ApplicationData> activeLoansFlux = applicationRepository
      .findByStatusAndLoanTypeAndEmail(savedApplication.getEmail(), ApplicationUseCaseKeys.ID_STATUS_APPROVED, null, ApplicationUseCaseKeys.PAGE_SIZE_DEFAULT_VALUE,
                                       ApplicationUseCaseKeys.PAGE_INIT_VALUE);
    return Mono.zip(userMono, activeLoansFlux.collectList(), loanTypeMono).flatMap(tuple -> {
      User user = tuple.getT1();
      List<ApplicationData> activeLoansData = tuple.getT2();
      LoanType loanType = tuple.getT3();
      if (Boolean.FALSE.equals(loanType.getAutomaticValidation())) {
        return Mono.just(savedApplication);
      }
      NewLoan newLoan = NewLoan
        .builder()
        .loanId(savedApplication.getApplicationId().toString())
        .requestedAmount(savedApplication.getAmount())
        .monthlyInterestRate(loanType.getInterestRate())
        .requestedTermMonths(savedApplication.getTerm())
        .build();
      List<ActiveLoan> activeLoans = activeLoansData
        .stream()
        .map(appData -> ActiveLoan
          .builder()
          .loanId(appData.getId().toString())
          .requestedAmount(appData.getAmount())
          .monthlyInterestRate(appData.getInterestRate())
          .requestedTermMonths(appData.getTerm())
          .build())
        .collect(Collectors.toList());
      return Mono.just(ValidationRequestEvent
        .builder()
        .applicantEmail(user.getEmail())
        .applicantBaseSalary(BigDecimal.valueOf(user.getBaseSalary()))
        .newLoan(newLoan)
        .activeLoans(activeLoans)
        .build())
        .flatMap(validationRequestEvent -> senderGateway.send(validationRequestEvent, QueueAlias.VALIDATIONS))
        .thenReturn(savedApplication);
    });
  }

  @Override
  public Mono<ApplicationList> getApplicationsByStatusAndLoanTypeAndEmail(String email, Integer status, Integer loanType, Integer pageSize, Integer pageNumber) {
    List<Mono<Void>> validations = new ArrayList<>();
    if (status != null) {
      validations.add(statusRepository.findById(Long.valueOf(status))
        .switchIfEmpty(Mono.error(new DomainValidationException(ErrorEnum.INVALID_APPLICATION_DATA, ApplicationUseCaseKeys.STATUS_ID_NOT_EXIST + status)))
        .then());
    }
    if (loanType != null) {
      validations.add(loanTypeRepository.findById(Long.valueOf(loanType))
        .switchIfEmpty(Mono.error(new DomainValidationException(ErrorEnum.INVALID_APPLICATION_DATA, ApplicationUseCaseKeys.LOAN_ID_NOT_EXIST + loanType)))
        .then());
    }
    if (email != null) {
      validations.add(userRestGateway.findUserByEmail(email)
        .switchIfEmpty(Mono.error(new DomainValidationException(ErrorEnum.INVALID_APPLICATION_DATA, ApplicationUseCaseKeys.EMAIL_NOT_EXIST + email)))
        .then());
    }
    return Mono.when(validations.toArray(new Mono[0]))
      .then(Mono.defer(() -> applicationRepository.countByStatusAndLoanTypeAndEmail(email, status, loanType)))
      .flatMap(totalRecords -> {
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        return applicationRepository.findByStatusAndLoanTypeAndEmail(email, status, loanType, pageSize, pageNumber)
          .map(app -> {
            List<PaymentPlan> paymentPlan = calculatePaymentPlan(app.getAmount(), app.getInterestRate(), app.getTerm());
            app.setTotalMonthlyPayment(paymentPlan.getFirst().principalForMonthNumber());
            return app;
          }).collectList()
          .map(applications -> ApplicationList
            .builder()
            .pageNumber(pageNumber)
            .pageSize(pageSize)
            .totalRecords(totalRecords.intValue())
            .totalPages(totalPages)
            .data(applications)
            .build());
      });
  }

  @Override
  public Mono<Application> getApplicationById(String id) {
    return applicationRepository.findById(id);
  }

  @Override
  public Mono<Application> patchApplicationStatus(Application application) {
    return statusRepository.findById(application.getStatusId())
      .switchIfEmpty(Mono.error(new DomainValidationException(ErrorEnum.INVALID_APPLICATION_DATA, ApplicationUseCaseKeys.STATUS_ID_NOT_EXIST + application.getStatusId())))
      .flatMap(status ->
        applicationRepository.saveApplication(application)
          .onErrorResume(e -> Mono.error(new DomainValidationException(ErrorEnum.INVALID_APPLICATION_DATA, ApplicationUseCaseKeys.ERROR_UPDATE)))
          .flatMap(savedApplication -> processStatusUpdate(savedApplication, status))
      );
  }

  private Mono<Application> processStatusUpdate(Application savedApplication, Status status) {
    boolean isApproved = Objects.equals(savedApplication.getStatusId(), ApplicationUseCaseKeys.APPROVED_STATUS_ID);
    boolean isRejected = Objects.equals(savedApplication.getStatusId(), ApplicationUseCaseKeys.REJECTED_STATUS_ID);
    if (!isApproved && !isRejected) {
      return Mono.just(savedApplication);
    }
    if (isApproved) {
      return handleApprovedStatus(savedApplication, status);
    }
    return handleRejectedStatus(savedApplication, status);
  }

  private Mono<Application> handleApprovedStatus(Application savedApplication, Status status) {
    return loanTypeRepository
      .findById(savedApplication.getLoanTypeId())
      .switchIfEmpty(Mono.error(new DomainValidationException(ErrorEnum.INVALID_APPLICATION_DATA, ApplicationUseCaseKeys.LOAN_ID_NOT_EXIST + savedApplication.getLoanTypeId())))
      .flatMap(loanType -> {
        List<PaymentPlan> paymentPlan = calculatePaymentPlan(savedApplication.getAmount(), loanType.getInterestRate(), savedApplication.getTerm());
        MessageBody body = MessageBody.builder()
          .subject(ApplicationUseCaseKeys.SUBJECT_UPDATE)
          .email(savedApplication.getEmail())
          .message(ApplicationUseCaseKeys.MESSAGE_UPDATE + status.getName())
          .paymentPlan(paymentPlan)
          .build();
        return senderGateway.send(body, QueueAlias.NOTIFICATIONS).thenReturn(savedApplication);
      });
  }

  private Mono<Application> handleRejectedStatus(Application savedApplication, Status status) {
    MessageBody body = MessageBody.builder()
      .subject(ApplicationUseCaseKeys.SUBJECT_UPDATE)
      .email(savedApplication.getEmail())
      .message(ApplicationUseCaseKeys.MESSAGE_UPDATE + status.getName())
      .build();
    return senderGateway.send(body, QueueAlias.NOTIFICATIONS).thenReturn(savedApplication);
  }

  private List<PaymentPlan> calculatePaymentPlan(BigDecimal principal, BigDecimal annualInterestRate, Integer termInMonths) {
    if (principal == null || annualInterestRate == null || termInMonths == null || termInMonths <= 0 || annualInterestRate.compareTo(BigDecimal.ZERO) < 0) {
      return Collections.emptyList();
    }
    BigDecimal monthlyPayment;
    if (annualInterestRate.compareTo(BigDecimal.ZERO) == 0) {
      monthlyPayment = principal.divide(BigDecimal.valueOf(termInMonths), 2, RoundingMode.HALF_UP);
    } else {
      BigDecimal monthlyRate = annualInterestRate.divide(PERCENTAGE_DIVISOR, MathContext.DECIMAL128).divide(MONTHS_IN_YEAR, MathContext.DECIMAL128);
      BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);
      BigDecimal onePlusRToTheN = onePlusR.pow(termInMonths, MathContext.DECIMAL128);
      BigDecimal denominator = onePlusRToTheN.subtract(BigDecimal.ONE);
      BigDecimal numerator = monthlyRate.multiply(onePlusRToTheN);
      monthlyPayment = principal.multiply(numerator).divide(denominator, 2, RoundingMode.HALF_UP);
    }
    List<PaymentPlan> plan = new ArrayList<>();
    BigDecimal remainingBalance = principal;
    LocalDate paymentDate = LocalDate.now().plusMonths(1);
    for (int i = 1; i <= termInMonths; i++) {
      BigDecimal interestForMonth = remainingBalance
        .multiply(annualInterestRate.divide(PERCENTAGE_DIVISOR, MathContext.DECIMAL128).divide(MONTHS_IN_YEAR, MathContext.DECIMAL128))
        .setScale(2, RoundingMode.HALF_UP);
      BigDecimal principalForMonth = monthlyPayment.subtract(interestForMonth);
      remainingBalance = remainingBalance.subtract(principalForMonth);
      PaymentPlan monthlyInstallment = new PaymentPlan(i, formatCurrency(principalForMonth), principalForMonth, formatCurrency(interestForMonth),
                                                       formatCurrency(monthlyPayment), formatCurrency(remainingBalance.max(BigDecimal.ZERO)));
      plan.add(monthlyInstallment);
      paymentDate = paymentDate.plusMonths(1);
    }
    return plan;
  }

  private String formatCurrency(BigDecimal amount) {
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.of("es", "CO"));
    return currencyFormatter.format(amount);
  }

}
