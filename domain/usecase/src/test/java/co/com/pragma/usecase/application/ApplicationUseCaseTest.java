package co.com.pragma.usecase.application;

import co.com.pragma.model.application.*;
import co.com.pragma.model.application.enums.QueueAlias;
import co.com.pragma.model.application.exception.DomainValidationException;
import co.com.pragma.model.application.exception.ErrorEnum;
import co.com.pragma.model.application.gateways.ApplicationRepository;
import co.com.pragma.model.application.gateways.LoanTypeRepository;
import co.com.pragma.model.application.gateways.SenderGateway;
import co.com.pragma.model.application.gateways.StatusRepository;
import co.com.pragma.model.application.gateways.UserRestGateway;
import co.com.pragma.usecase.application.constants.ApplicationUseCaseKeys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationUseCaseTest {

  private Application validApplication;
  private LoanType validLoanType;
  private User validUser;

  @Mock
  private ApplicationRepository applicationRepository;
  @Mock
  private StatusRepository statusRepository;
  @Mock
  private LoanTypeRepository loanTypeRepository;
  @Mock
  private SenderGateway senderGateway;
  @Mock
  private UserRestGateway userRestGateway;
  @InjectMocks
  private ApplicationUseCase applicationUseCase;


  @BeforeEach
  void setUp() {
    validApplication = Application
      .builder()
      .applicationId(UUID.randomUUID())
      .amount(new BigDecimal("1000.00"))
      .term(12)
      .email("test@example.com")
      .identityDocument(123456789L)
      .statusId(1L)
      .loanTypeId(1L)
      .build();
    validLoanType = LoanType
      .builder()
      .loanTypeId(1L)
      .name("Personal Loan")
      .minimumAmount(new BigDecimal("100.00"))
      .maximumAmount(new BigDecimal("10000.00"))
      .interestRate(new BigDecimal("0.05"))
      .automaticValidation(true)
      .build();
    validUser = User.builder().email("test@example.com").baseSalary(2000000L).build();
  }

  @Test
  void saveApplication_success() {
    when(loanTypeRepository.findById(any(Long.class))).thenReturn(Mono.just(validLoanType));
    when(applicationRepository.saveApplication(any(Application.class))).thenReturn(Mono.just(validApplication));
    when(userRestGateway.findUserByEmail(anyString())).thenReturn(Mono.just(validUser));
    when(applicationRepository.findByStatusAndLoanTypeAndEmail(anyString(), anyInt(), any(), anyInt(), anyInt())).thenReturn(Flux.empty());
    when(senderGateway.send(any(), any(QueueAlias.class))).thenReturn(Mono.empty());
    StepVerifier.create(applicationUseCase.saveApplication(validApplication)).expectNext(validApplication).verifyComplete();
  }

  @Test
  void saveApplication_loanTypeNotFound() {
    when(loanTypeRepository.findById(any(Long.class))).thenReturn(Mono.empty());
    StepVerifier
      .create(applicationUseCase.saveApplication(validApplication))
      .expectErrorMatches(throwable -> throwable instanceof DomainValidationException &&
                                       throwable.getMessage().equals(ApplicationUseCaseKeys.LOAN_ID_NOT_EXIST + validApplication.getLoanTypeId()))
      .verify();
  }

  @Test
  void getApplicationsByStatusAndLoanTypeAndEmail_success() {
    String email = "test@example.com";
    Integer status = 2;
    Integer loanType = 1;
    Integer pageSize = 10;
    Integer pageNumber = 1;
    Long totalRecords = 2L;
    ApplicationData appData1 = new ApplicationData();
    appData1.setId(UUID.randomUUID());
    appData1.setAmount(new BigDecimal("10000.00"));
    appData1.setTerm(12);
    appData1.setInterestRate(new BigDecimal("10.00")); // 10% annual
    appData1.setStatusId(2L);
    appData1.setApplicationStatus("Aprobada");
    ApplicationData appData2 = new ApplicationData();
    appData2.setId(UUID.randomUUID());
    appData2.setAmount(new BigDecimal("20000.00"));
    appData2.setTerm(24);
    appData2.setInterestRate(new BigDecimal("5.00")); // 5% annual
    appData2.setStatusId(2L);
    appData2.setApplicationStatus("Aprobada");

    when(statusRepository.findById(any(Long.class))).thenReturn(Mono.just(Status.builder().statusId(2L).name("Aprobada").build()));
    when(loanTypeRepository.findById(any(Long.class))).thenReturn(Mono.just(validLoanType));
    when(userRestGateway.findUserByEmail(anyString())).thenReturn(Mono.just(validUser));
    when(applicationRepository.countByStatusAndLoanTypeAndEmail(anyString(), anyInt(), anyInt())).thenReturn(Mono.just(totalRecords));
    when(applicationRepository.findByStatusAndLoanTypeAndEmail(anyString(), anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(Flux.just(appData1, appData2));

    Mono<ApplicationList> result = applicationUseCase.getApplicationsByStatusAndLoanTypeAndEmail(email, status, loanType, pageSize, pageNumber);

    StepVerifier.create(result).assertNext(applicationList -> {
      assertAll(
        () -> assertNotNull(applicationList),
        () -> assertEquals(pageNumber, applicationList.pageNumber()),
        () -> assertEquals(pageSize, applicationList.pageSize()),
        () -> assertEquals(totalRecords.intValue(), applicationList.totalRecords()),
        () -> assertEquals(1, applicationList.totalPages()),
        () -> assertNotNull(applicationList.data()),
        () -> assertEquals(2, applicationList.data().size())
        //() -> assertEquals(new BigDecimal("879.16"), applicationList.data().get(0).getTotalMonthlyPayment()),
        //() -> assertEquals(new BigDecimal("877.43"), applicationList.data().get(1).getTotalMonthlyPayment())
      );
    }).verifyComplete();
  }

  @Test
  void getApplicationsByStatusAndLoanTypeAndEmail_noRecords() {
    Integer status = 1;
    Integer loanType = 1;
    Integer pageSize = 10;
    Integer pageNumber = 1;
    Long totalRecords = 0L;
    when(statusRepository.findById(any(Long.class))).thenReturn(Mono.just(Status.builder().statusId(1L).name("Pendiente").build()));
    when(loanTypeRepository.findById(any(Long.class))).thenReturn(Mono.just(validLoanType));
    when(applicationRepository.countByStatusAndLoanTypeAndEmail(isNull(), anyInt(), anyInt())).thenReturn(Mono.just(totalRecords));
    when(applicationRepository.findByStatusAndLoanTypeAndEmail(isNull(), anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(Flux.empty());
    Mono<ApplicationList> result = applicationUseCase.getApplicationsByStatusAndLoanTypeAndEmail(null, status, loanType, pageSize, pageNumber);
    StepVerifier.create(result).assertNext(applicationList -> {
      assertNotNull(applicationList);
      assertEquals(pageNumber, applicationList.pageNumber());
      assertEquals(pageSize, applicationList.pageSize());
      assertEquals(totalRecords.intValue(), applicationList.totalRecords());
      assertEquals(0, applicationList.totalPages());
      assertNotNull(applicationList.data());
      assertEquals(0, applicationList.data().size());
    }).verifyComplete();
  }

  @Test
  void getApplicationsByStatusAndLoanTypeAndEmail_statusNotFound() {
    Integer status = 99;
    String email = "test@test.com";
    Integer loanType = 1;

    // This is the key mock for the test
    when(statusRepository.findById(any(Long.class))).thenReturn(Mono.empty());

    // Add other mocks to prevent other errors, even if they are not the focus
    when(loanTypeRepository.findById(any(Long.class))).thenReturn(Mono.just(validLoanType));
    when(userRestGateway.findUserByEmail(anyString())).thenReturn(Mono.just(validUser));

    Mono<ApplicationList> result = applicationUseCase.getApplicationsByStatusAndLoanTypeAndEmail(email, status, loanType, 10, 1);

    StepVerifier.create(result)
      .expectErrorMatches(throwable -> throwable instanceof DomainValidationException &&
        throwable.getMessage().equals(ApplicationUseCaseKeys.STATUS_ID_NOT_EXIST + status))
      .verify();
  }

  @Test
  void getApplicationById_success() {
    String applicationId = validApplication.getApplicationId().toString();
    when(applicationRepository.findById(applicationId)).thenReturn(Mono.just(validApplication));

    StepVerifier.create(applicationUseCase.getApplicationById(applicationId))
      .expectNext(validApplication)
      .verifyComplete();
  }

  private Application buildModifiedApplication(Long newStatusId) {
    return Application.builder()
            .applicationId(validApplication.getApplicationId())
            .amount(validApplication.getAmount())
            .term(validApplication.getTerm())
            .email(validApplication.getEmail())
            .identityDocument(validApplication.getIdentityDocument())
            .loanTypeId(validApplication.getLoanTypeId())
            .statusId(newStatusId)
            .build();
  }

  @Test
  void patchApplicationStatus_success_approvedStatus() {
    Application updatedApplication = buildModifiedApplication(2L);
    Status approvedStatus = Status.builder().statusId(2L).name("Aprobada").build();

    when(statusRepository.findById(2L)).thenReturn(Mono.just(approvedStatus));
    when(applicationRepository.saveApplication(any(Application.class))).thenReturn(Mono.just(updatedApplication));
    when(loanTypeRepository.findById(any(Long.class))).thenReturn(Mono.just(validLoanType));
    when(senderGateway.send(any(MessageBody.class), any(QueueAlias.class))).thenReturn(Mono.empty());

    StepVerifier.create(applicationUseCase.patchApplicationStatus(updatedApplication))
      .expectNext(updatedApplication)
      .verifyComplete();
  }

  @Test
  void patchApplicationStatus_success_rejectedStatus() {
    Application updatedApplication = buildModifiedApplication(3L);
    Status rejectedStatus = Status.builder().statusId(3L).name("Rechazada").build();

    when(statusRepository.findById(3L)).thenReturn(Mono.just(rejectedStatus));
    when(applicationRepository.saveApplication(any(Application.class))).thenReturn(Mono.just(updatedApplication));
    when(senderGateway.send(any(MessageBody.class), any(QueueAlias.class))).thenReturn(Mono.empty());

    StepVerifier.create(applicationUseCase.patchApplicationStatus(updatedApplication))
      .expectNext(updatedApplication)
      .verifyComplete();
  }

  @Test
  void patchApplicationStatus_noStatusChange() {
    Application updatedApplication = buildModifiedApplication(1L);
    Status pendingStatus = Status.builder().statusId(1L).name("Pendiente").build();

    when(statusRepository.findById(1L)).thenReturn(Mono.just(pendingStatus));
    when(applicationRepository.saveApplication(any(Application.class))).thenReturn(Mono.just(updatedApplication));

    StepVerifier.create(applicationUseCase.patchApplicationStatus(updatedApplication))
      .expectNext(updatedApplication)
      .verifyComplete();
  }

  @Test
  void patchApplicationStatus_error_on_save() {
    Status pendingStatus = Status.builder().statusId(1L).name("Pendiente").build();
    when(statusRepository.findById(any(Long.class))).thenReturn(Mono.just(pendingStatus));
    when(applicationRepository.saveApplication(any(Application.class))).thenReturn(Mono.error(new RuntimeException("DB Error")));

    StepVerifier.create(applicationUseCase.patchApplicationStatus(validApplication))
      .expectErrorMatches(throwable -> throwable instanceof DomainValidationException &&
                                       throwable.getMessage().contains(ApplicationUseCaseKeys.ERROR_UPDATE))
      .verify();
  }

  @Test
  void patchApplicationStatus_status_not_exist() {
    Application updatedApplication = buildModifiedApplication(99L);
    when(statusRepository.findById(99L)).thenReturn(Mono.empty());

    StepVerifier.create(applicationUseCase.patchApplicationStatus(updatedApplication))
      .expectErrorMatches(throwable -> throwable instanceof DomainValidationException &&
                                       throwable.getMessage().equals(ApplicationUseCaseKeys.STATUS_ID_NOT_EXIST + updatedApplication.getStatusId()))
      .verify();
  }

  @Test
  void patchApplicationStatus_sender_error() {
    Application updatedApplication = buildModifiedApplication(2L);
    Status approvedStatus = Status.builder().statusId(2L).name("Aprobada").build();

    when(statusRepository.findById(2L)).thenReturn(Mono.just(approvedStatus));
    when(applicationRepository.saveApplication(any(Application.class))).thenReturn(Mono.just(updatedApplication));
    when(loanTypeRepository.findById(any(Long.class))).thenReturn(Mono.just(validLoanType));
    when(senderGateway.send(any(MessageBody.class), any(QueueAlias.class))).thenReturn(Mono.error(new RuntimeException("Sender Error")));

    StepVerifier.create(applicationUseCase.patchApplicationStatus(updatedApplication))
            .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                    throwable.getMessage().equals("Sender Error"))
            .verify();
  }
}
