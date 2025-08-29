package co.com.pragma.r2dbc;

import co.com.pragma.model.application.LoanType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanTypeReactiveRepositoryAdapterTest {

  @Mock
  private LoanTypeReactiveRepository repository;
  @Mock
  private ObjectMapper mapper;

  @InjectMocks
  private LoanTypeReactiveRepositoryAdapter adapter;

  @Test
  void findByIdShouldReturnLoanType() {
    Long loanTypeId = 1L;
    LoanTypeEntity loanTypeEntity = new LoanTypeEntity(loanTypeId, "Personal Loan", new BigDecimal("1000.00"), new BigDecimal("10000.00"), new BigDecimal("0.05"), true);
    LoanType loanType = LoanType.builder().loanTypeId(loanTypeId).build();
    when(repository.findById(loanTypeId)).thenReturn(Mono.just(loanTypeEntity));
    when(mapper.mapBuilder(loanTypeEntity, LoanType.Builder.class)).thenReturn(LoanType.builder().loanTypeId(loanTypeEntity.getLoanTypeId()));
    Mono<LoanType> result = adapter.findById(loanTypeId);
    StepVerifier.create(result)
      .assertNext(
        lt -> assertAll(() -> assertEquals(loanType.getLoanTypeId(), lt.getLoanTypeId()))
      ).verifyComplete();
    verify(repository, times(1)).findById(loanTypeId);
    verify(mapper, times(1)).mapBuilder(loanTypeEntity, LoanType.Builder.class);
  }

  @Test
  void findByIdShouldReturnEmptyMonoWhenNotFound() {
    Long loanTypeId = 1L;
    when(repository.findById(loanTypeId)).thenReturn(Mono.empty());
    Mono<LoanType> result = adapter.findById(loanTypeId);
    StepVerifier.create(result).expectComplete().verify();
    verify(repository, times(1)).findById(loanTypeId);
    verify(mapper, never()).mapBuilder(any(), any());
  }

}
