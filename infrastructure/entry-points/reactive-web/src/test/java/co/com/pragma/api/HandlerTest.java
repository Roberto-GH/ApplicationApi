package co.com.pragma.api;

import co.com.pragma.api.dto.ApplicationResponseDto;
import co.com.pragma.api.dto.CreateApplicationDto;
import co.com.pragma.api.exception.ApplicationApiException;
import co.com.pragma.api.mapper.ApplicationDtoMapper;
import co.com.pragma.model.application.Application;
import co.com.pragma.model.application.validation.DomainValidationException;
import co.com.pragma.usecase.application.adapters.ApplicationControllerUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HandlerTest {

  private CreateApplicationDto createApplicationDto;
  private Application application;
  private ApplicationResponseDto applicationResponseDto;

  @Mock
  private ApplicationControllerUseCase applicationControllerUseCase;
  @Mock
  private ApplicationDtoMapper applicationDtoMapper;
  @Mock
  private TransactionalOperator transactionalOperator;
  @InjectMocks
  private Handler handler;


  @BeforeEach
  void setUp() {
    createApplicationDto = new CreateApplicationDto(
      new BigDecimal("1000.00"), "12 months", "test@example.com", 123456789L, 1L
    );
    application = Application.builder().build();
    applicationResponseDto = new ApplicationResponseDto(
      application.getApplicationId(), application.getAmount(), application.getTerm(), application.getEmail(),
      application.getIdentityDocument(), application.getStatusId(), application.getLoanTypeId()
    );
  }

  @Test
  void listenSaveApplication_success() {
    MockServerRequest request = MockServerRequest.builder().body(Mono.just(createApplicationDto));
    when(applicationDtoMapper.toModel(any(CreateApplicationDto.class))).thenReturn(Application.builder());
    when(applicationControllerUseCase.saveApplication(any(Application.class))).thenReturn(Mono.just(application));
    when(transactionalOperator.transactional(any(Mono.class))).thenReturn(Mono.just(application));
    when(applicationDtoMapper.toResponseDto(any(Application.class))).thenReturn(applicationResponseDto);
    StepVerifier.create(handler.listenSaveApplication(request)).expectNextMatches(serverResponse -> {
      assertEquals(HttpStatus.OK, serverResponse.statusCode());
      assertEquals(MediaType.APPLICATION_JSON, serverResponse.headers().getContentType());
      return true;
    }).verifyComplete();
  }

  @Test
  void listenSaveApplication_emptyBody() {
    MockServerRequest request = MockServerRequest.builder().body(Mono.empty());
    StepVerifier
      .create(handler.listenSaveApplication(request))
      .expectErrorMatches(
        throwable -> throwable instanceof ApplicationApiException
                     && ((ApplicationApiException) throwable).getStatus().equals(HttpStatus.BAD_REQUEST)
                     && throwable.getMessage().equals(ApplicationWebKeys.ERROR_DATA_REQUIRED)
      ).verify();
  }

  @Test
  void listenSaveApplication_domainValidationException() {
    MockServerRequest request = MockServerRequest.builder().body(Mono.just(createApplicationDto));
    when(applicationDtoMapper.toModel(any(CreateApplicationDto.class))).thenReturn(Application.builder());
    when(applicationControllerUseCase.saveApplication(any(Application.class))).thenReturn(Mono.error(new DomainValidationException("Invalid data", HttpStatus.BAD_REQUEST.value())));
    when(transactionalOperator.transactional(any(Mono.class))).thenReturn(Mono.error(new DomainValidationException("Invalid data", HttpStatus.BAD_REQUEST.value())));
    StepVerifier
      .create(handler.listenSaveApplication(request))
      .expectErrorMatches(throwable -> throwable instanceof DomainValidationException && throwable.getMessage().equals("Invalid data"))
      .verify();
  }

}
