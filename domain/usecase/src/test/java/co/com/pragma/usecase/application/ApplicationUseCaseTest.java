package co.com.pragma.usecase.application;

import co.com.pragma.model.application.*;
import co.com.pragma.model.application.exception.DomainValidationException;
import co.com.pragma.model.application.gateways.ApplicationRepository;
import co.com.pragma.model.application.gateways.LoanTypeRepository;
import co.com.pragma.model.application.gateways.StatusRepository;
import co.com.pragma.usecase.application.constants.ApplicationUseCaseKeys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationUseCaseTest {

  private Application validApplication;
  private LoanType validLoanType;

  @Mock
  private ApplicationRepository applicationRepository;
  @Mock
  private StatusRepository statusRepository;
  @Mock
  private LoanTypeRepository loanTypeRepository;
  @Mock
  private co.com.pragma.model.application.gateways.SenderGateway senderGateway;
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
  }

  @Test
  void saveApplication_success() {
    when(loanTypeRepository.findById(any(Long.class))).thenReturn(Mono.just(validLoanType));
    when(applicationRepository.saveApplication(any(Application.class))).thenReturn(Mono.just(validApplication));
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
  void getApplicationsByStatusAndLoanType_success() {
    Integer status = 2;
    Integer loanType = 1;
    Integer pageSize = 10;
    Integer pageNumber = 0;
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

    when(applicationRepository.countByStatusAndLoanType(anyInt(), anyInt())).thenReturn(Mono.just(totalRecords));
    when(applicationRepository.findByStatusAndLoanType(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(Flux.just(appData1, appData2));

    Mono<ApplicationList> result = applicationUseCase.getApplicationsByStatusAndLoanType(status, loanType, pageSize, pageNumber);

    StepVerifier.create(result).assertNext(applicationList -> {
      assertAll(
        () -> assertNotNull(applicationList),
        () -> assertEquals(pageNumber, applicationList.pageNumber()),
        () -> assertEquals(pageSize, applicationList.pageSize()),
        () -> assertEquals(totalRecords.intValue(), applicationList.totalRecords()),
        () -> assertEquals(1, applicationList.totalPages()),
        () -> assertNotNull(applicationList.data()),
        () -> assertEquals(2, applicationList.data().size()),
        () -> assertEquals(new BigDecimal("879.16"), applicationList.data().get(0).getTotalMonthlyPayment()),
        () -> assertEquals(new BigDecimal("877.43"), applicationList.data().get(1).getTotalMonthlyPayment())
      );
    }).verifyComplete();
  }

  @Test
  void getApplicationsByStatusAndLoanType_noRecords() {
    Integer status = 1;
    Integer loanType = 1;
    Integer pageSize = 10;
    Integer pageNumber = 0;
    Long totalRecords = 0L;
    when(statusRepository.findById(any(Long.class))).thenReturn(Mono.just(Status.builder().statusId(2L).name("Aprobada").build()));
    when(loanTypeRepository.findById(any(Long.class))).thenReturn(Mono.just(validLoanType));
    when(applicationRepository.countByStatusAndLoanType(anyInt(), anyInt())).thenReturn(Mono.just(totalRecords));
    when(applicationRepository.findByStatusAndLoanType(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(Flux.empty());
    Mono<ApplicationList> result = applicationUseCase.getApplicationsByStatusAndLoanType(status, loanType, pageSize, pageNumber);
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

  @ParameterizedTest
  @CsvSource(value = {
    "12000.00,12,10.00,2,1054.99",
    "12000.00,12,0,2,1000.00",
    ",12,0,2,0",
    "12000.00,12,,2,0",
    "12000.00,,10.00,2,0",
    "12000.00,0,10.00,2,0",
    "12000.00,12,0.00,2,1000.00",
    "12000.00,-1,10.00,2,0",
    "12000.00,12,-1.00,2,0",
    "12000.00,12,10.00,1,"
  })
  void getApplicationsByStatusAndLoanType_calculationEdgeCases(BigDecimal amount, Integer term, BigDecimal interestRate, Long statusId, BigDecimal total) {
    when(statusRepository.findById(any(Long.class))).thenReturn(Mono.just(Status.builder().statusId(2L).name("Aprobada").build()));
    when(loanTypeRepository.findById(any(Long.class))).thenReturn(Mono.just(validLoanType));
    when(applicationRepository.countByStatusAndLoanType(anyInt(), anyInt())).thenReturn(Mono.just(1L));
    when(applicationRepository.findByStatusAndLoanType(anyInt(), anyInt(), anyInt(), anyInt()))
      .thenAnswer(invocation -> {
        List<ApplicationData> processedData = new ArrayList<>();
        ApplicationData appData1 = new ApplicationData();
        appData1.setAmount(amount);
        appData1.setTerm(term);
        appData1.setInterestRate(interestRate);
        appData1.setStatusId(statusId);
        processedData.add(appData1);
        return Flux.fromIterable(processedData);
      });
    Mono<ApplicationList> result = applicationUseCase.getApplicationsByStatusAndLoanType(2, 1, 10, 1);
    StepVerifier.create(result)
      .assertNext(applicationList -> {
        assertEquals(total, applicationList.data().getFirst().getTotalMonthlyPayment());
    }).verifyComplete();
  }

  @Test
  void getApplicationsByStatusAndLoanType_negativeInterestRate() {
    Integer status = 2;
    Integer loanType = 1;
    Integer pageSize = 10;
    Integer pageNumber = 0;
    Long totalRecords = 1L;
    ApplicationData appData9 = new ApplicationData();
    appData9.setAmount(new BigDecimal("1000.00"));
    appData9.setTerm(12);
    appData9.setInterestRate(new BigDecimal("-5.00"));
    appData9.setStatusId(2L);
    appData9.setTotalMonthlyPayment(BigDecimal.ZERO);
    when(statusRepository.findById(any(Long.class))).thenReturn(Mono.just(Status.builder().statusId(2L).name("Aprobada").build()));
    when(loanTypeRepository.findById(any(Long.class))).thenReturn(Mono.just(validLoanType));
    when(applicationRepository.countByStatusAndLoanType(anyInt(), anyInt())).thenReturn(Mono.just(totalRecords));
    when(applicationRepository.findByStatusAndLoanType(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(Flux.just(appData9));

    Mono<ApplicationList> result = applicationUseCase.getApplicationsByStatusAndLoanType(status, loanType, pageSize, pageNumber);

    StepVerifier.create(result).assertNext(applicationList -> {
      assertNotNull(applicationList);
      assertEquals(pageNumber, applicationList.pageNumber());
      assertEquals(pageSize, applicationList.pageSize());
      assertEquals(totalRecords.intValue(), applicationList.totalRecords());
      assertEquals(1, applicationList.totalPages());
      assertNotNull(applicationList.data());
      assertEquals(1, applicationList.data().size());
      assertEquals(0, applicationList.data().get(0).getTotalMonthlyPayment().compareTo(BigDecimal.ZERO));
    }).verifyComplete();
  }

  @Test
  void getApplicationsByStatusAndLoanType_onlyStatusProvided_success() {
    Integer status = 2;
    Integer pageSize = 10;
    Integer pageNumber = 0;
    Long totalRecords = 0L;

    when(statusRepository.findById(any(Long.class))).thenReturn(Mono.just(Status.builder().statusId(2L).name("Aprobada").build()));
    when(applicationRepository.countByStatusAndLoanType(anyInt(), any())).thenReturn(Mono.just(totalRecords));
    when(applicationRepository.findByStatusAndLoanType(anyInt(), any(), anyInt(), anyInt())).thenReturn(Flux.empty());

    Mono<ApplicationList> result = applicationUseCase.getApplicationsByStatusAndLoanType(status, null, pageSize, pageNumber);

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
  void getApplicationsByStatusAndLoanType_onlyLoanTypeProvided_success() {
    Integer loanType = 1;
    Integer pageSize = 10;
    Integer pageNumber = 0;
    Long totalRecords = 0L;

    when(loanTypeRepository.findById(any(Long.class))).thenReturn(Mono.just(validLoanType));
    when(applicationRepository.countByStatusAndLoanType(any(), anyInt())).thenReturn(Mono.just(totalRecords));
    when(applicationRepository.findByStatusAndLoanType(any(), anyInt(), anyInt(), anyInt())).thenReturn(Flux.empty());

    Mono<ApplicationList> result = applicationUseCase.getApplicationsByStatusAndLoanType(null, loanType, pageSize, pageNumber);

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
  void getApplicationById_success() {
    String applicationId = validApplication.getApplicationId().toString();
    when(applicationRepository.findById(applicationId)).thenReturn(Mono.just(validApplication));

    StepVerifier.create(applicationUseCase.getApplicationById(applicationId))
      .expectNext(validApplication)
      .verifyComplete();
  }

  @Test
  void patchApplicationStatus_success_approvedStatus() {
    Application updatedApplication = Application.builder()
      .applicationId(validApplication.getApplicationId())
      .amount(validApplication.getAmount())
      .term(validApplication.getTerm())
      .email(validApplication.getEmail())
      .identityDocument(validApplication.getIdentityDocument())
      .statusId(2L) // APPROVED_STATUS_ID
      .loanTypeId(validApplication.getLoanTypeId())
      .build();
    Status approvedStatus = Status.builder().statusId(2L).name("Aprobada").build();

    when(applicationRepository.saveApplication(any(Application.class))).thenReturn(Mono.just(updatedApplication));
    when(statusRepository.findById(2L)).thenReturn(Mono.just(approvedStatus));
    when(senderGateway.send(any(MessageBody.class))).thenReturn(Mono.just("Message sent successfully"));

    StepVerifier.create(applicationUseCase.patchApplicationStatus(updatedApplication))
      .expectNext(updatedApplication)
      .verifyComplete();
  }

  @Test
  void patchApplicationStatus_success_rejectedStatus() {
    Application updatedApplication = Application.builder()
      .applicationId(validApplication.getApplicationId())
      .amount(validApplication.getAmount())
      .term(validApplication.getTerm())
      .email(validApplication.getEmail())
      .identityDocument(validApplication.getIdentityDocument())
      .statusId(3L) // REJECTED_STATUS_ID
      .loanTypeId(validApplication.getLoanTypeId())
      .build();
    Status rejectedStatus = Status.builder().statusId(3L).name("Rechazada").build();

    when(applicationRepository.saveApplication(any(Application.class))).thenReturn(Mono.just(updatedApplication));
    when(statusRepository.findById(3L)).thenReturn(Mono.just(rejectedStatus));
    when(senderGateway.send(any(MessageBody.class))).thenReturn(Mono.just("Message sent successfully"));

    StepVerifier.create(applicationUseCase.patchApplicationStatus(updatedApplication))
      .expectNext(updatedApplication)
      .verifyComplete();
  }

  @Test
  void patchApplicationStatus_noStatusChange() {
    Application updatedApplication = Application.builder()
      .applicationId(validApplication.getApplicationId())
      .amount(validApplication.getAmount())
      .term(validApplication.getTerm())
      .email(validApplication.getEmail())
      .identityDocument(validApplication.getIdentityDocument())
      .statusId(1L) // Some other status, not approved or rejected
      .loanTypeId(validApplication.getLoanTypeId())
      .build();
    when(applicationRepository.saveApplication(any(Application.class))).thenReturn(Mono.just(updatedApplication));

    StepVerifier.create(applicationUseCase.patchApplicationStatus(updatedApplication))
      .expectNext(updatedApplication)
      .verifyComplete();
  }

  @Test
  void patchApplicationStatus_error_update() {
    when(applicationRepository.saveApplication(any(Application.class))).thenReturn(Mono.error(new RuntimeException("DB Error")));

    StepVerifier.create(applicationUseCase.patchApplicationStatus(validApplication))
      .expectErrorMatches(throwable -> throwable instanceof DomainValidationException &&
                                       throwable.getMessage().equals("Error update application "))
      .verify();
  }

  @Test
  void patchApplicationStatus_status_not_exist() {
    Application updatedApplication = Application.builder()
      .applicationId(validApplication.getApplicationId())
      .amount(validApplication.getAmount())
      .term(validApplication.getTerm())
      .email(validApplication.getEmail())
      .identityDocument(validApplication.getIdentityDocument())
      .statusId(2L) // APPROVED_STATUS_ID
      .loanTypeId(validApplication.getLoanTypeId())
      .build();
    when(applicationRepository.saveApplication(any(Application.class))).thenReturn(Mono.just(updatedApplication));
    when(statusRepository.findById(2L)).thenReturn(Mono.empty());

    StepVerifier.create(applicationUseCase.patchApplicationStatus(updatedApplication))
      .expectErrorMatches(throwable -> throwable instanceof DomainValidationException &&
                                       throwable.getMessage().equals("Status with id does not exist " + updatedApplication.getStatusId()))
      .verify();
  }

  @Test
  void patchApplicationStatus_internal_conflict_server() {
    Application updatedApplication = Application.builder()
      .applicationId(validApplication.getApplicationId())
      .amount(validApplication.getAmount())
      .term(validApplication.getTerm())
      .email(validApplication.getEmail())
      .identityDocument(validApplication.getIdentityDocument())
      .statusId(2L) // APPROVED_STATUS_ID
      .loanTypeId(validApplication.getLoanTypeId())
      .build();
    Status approvedStatus = Status.builder().statusId(2L).name("Aprobada").build();

    when(applicationRepository.saveApplication(any(Application.class))).thenReturn(Mono.just(updatedApplication));
    when(statusRepository.findById(2L)).thenReturn(Mono.just(approvedStatus));
    when(senderGateway.send(any(MessageBody.class))).thenReturn(Mono.error(new RuntimeException("Sender Error")));

    StepVerifier.create(applicationUseCase.patchApplicationStatus(updatedApplication))
      .expectErrorMatches(throwable -> throwable instanceof DomainValidationException &&
                                       throwable.getMessage().equals("Internal server conflic."))
      .verify();
  }

}