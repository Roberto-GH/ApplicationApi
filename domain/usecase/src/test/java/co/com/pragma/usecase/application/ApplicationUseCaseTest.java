package co.com.pragma.usecase.application;

import co.com.pragma.model.application.Application;
import co.com.pragma.model.application.LoanType;
import co.com.pragma.model.application.gateways.ApplicationRepository;
import co.com.pragma.model.application.gateways.LoanTypeRepository;
import co.com.pragma.model.application.validation.DomainValidationException;
import co.com.pragma.usecase.application.constants.ApplicationUseCaseKeys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationUseCaseTest {

  @Mock
  private ApplicationRepository applicationRepository;
  @Mock
  private LoanTypeRepository loanTypeRepository;

  @InjectMocks
  private ApplicationUseCase applicationUseCase;
  private Application validApplication;
  private LoanType validLoanType;

  @BeforeEach
  void setUp() {
    validApplication = Application
      .builder()
      .applicationId(UUID.randomUUID())
      .amount(new BigDecimal("1000.00"))
      .term("12 months")
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
          .expectErrorMatches(
            throwable -> throwable instanceof DomainValidationException &&
                         throwable.getMessage().equals(ApplicationUseCaseKeys.LOAD_ID_NO_EXIST + validApplication.getLoanTypeId()))
          .verify();
    }

}