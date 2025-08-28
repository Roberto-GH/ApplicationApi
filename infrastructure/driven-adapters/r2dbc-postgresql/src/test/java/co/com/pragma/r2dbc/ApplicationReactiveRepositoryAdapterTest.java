package co.com.pragma.r2dbc;

import co.com.pragma.model.application.Application;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationReactiveRepositoryAdapterTest {

  @InjectMocks
  ApplicationReactiveRepositoryAdapter repositoryAdapter;
  @Mock
  ApplicationReactiveRepository repository;
  @Mock
  ObjectMapper mapper;

  @Test
  void mustFindValueById() {
    UUID applicationId = UUID.randomUUID();
    ApplicationEntity applicationEntity = new ApplicationEntity(applicationId, new BigDecimal("1000.00"), "12 months", "test@example.com", 123456789L, 1L, 2L);
    Application application = Application
      .builder()
      .applicationId(applicationId)
      .amount(new BigDecimal("1000.00"))
      .term("12 months")
      .email("test@example.com")
      .identityDocument(123456789L)
      .statusId(1L)
      .loanTypeId(2L)
      .build();
    when(repository.findById(applicationId)).thenReturn(Mono.just(applicationEntity));
    when(mapper.mapBuilder(applicationEntity, Application.Builder.class)).thenReturn(Application
                                                                                       .builder()
                                                                                       .applicationId(applicationEntity.getApplicationId())
                                                                                       .amount(applicationEntity.getAmount())
                                                                                       .term(applicationEntity.getTerm())
                                                                                       .email(applicationEntity.getEmail())
                                                                                       .identityDocument(applicationEntity.getIdentityDocument())
                                                                                       .statusId(applicationEntity.getStatusId())
                                                                                       .loanTypeId(applicationEntity.getLoanTypeId()));
    Mono<Application> result = repositoryAdapter.findById(applicationId);
    StepVerifier
      .create(result)
      .assertNext(app -> assertAll(() -> assertEquals(application.getApplicationId(), app.getApplicationId()), () -> assertEquals(application.getAmount(), app.getAmount()),
                                   () -> assertEquals(application.getTerm(), app.getTerm()), () -> assertEquals(application.getEmail(), app.getEmail()),
                                   () -> assertEquals(application.getIdentityDocument(), app.getIdentityDocument()),
                                   () -> assertEquals(application.getStatusId(), app.getStatusId()), () -> assertEquals(application.getLoanTypeId(), app.getLoanTypeId())))
      .verifyComplete();
  }

  @Test
  void mustFindAllValues() {
    UUID applicationId1 = UUID.randomUUID();
    UUID applicationId2 = UUID.randomUUID();
    ApplicationEntity applicationEntity1 = new ApplicationEntity(applicationId1, new BigDecimal("1000.00"), "12 months", "test1@example.com", 123L, 1L, 2L);
    ApplicationEntity applicationEntity2 = new ApplicationEntity(applicationId2, new BigDecimal("2000.00"), "24 months", "test2@example.com", 456L, 3L, 4L);
    Application application1 = Application
      .builder()
      .applicationId(applicationId1)
      .amount(new BigDecimal("1000.00"))
      .term("12 months")
      .email("test1@example.com")
      .identityDocument(123L)
      .statusId(1L)
      .loanTypeId(2L)
      .build();
    Application application2 = Application
      .builder()
      .applicationId(applicationId2)
      .amount(new BigDecimal("2000.00"))
      .term("24 months")
      .email("test2@example.com")
      .identityDocument(456L)
      .statusId(3L)
      .loanTypeId(4L)
      .build();
    when(repository.findAll()).thenReturn(Flux.just(applicationEntity1, applicationEntity2));
    when(mapper.mapBuilder(applicationEntity1, Application.Builder.class)).thenReturn(Application
                                                                                        .builder()
                                                                                        .applicationId(applicationEntity1.getApplicationId())
                                                                                        .amount(applicationEntity1.getAmount())
                                                                                        .term(applicationEntity1.getTerm())
                                                                                        .email(applicationEntity1.getEmail())
                                                                                        .identityDocument(applicationEntity1.getIdentityDocument())
                                                                                        .statusId(applicationEntity1.getStatusId())
                                                                                        .loanTypeId(applicationEntity1.getLoanTypeId()));
    when(mapper.mapBuilder(applicationEntity2, Application.Builder.class)).thenReturn(Application
                                                                                        .builder()
                                                                                        .applicationId(applicationEntity2.getApplicationId())
                                                                                        .amount(applicationEntity2.getAmount())
                                                                                        .term(applicationEntity2.getTerm())
                                                                                        .email(applicationEntity2.getEmail())
                                                                                        .identityDocument(applicationEntity2.getIdentityDocument())
                                                                                        .statusId(applicationEntity2.getStatusId())
                                                                                        .loanTypeId(applicationEntity2.getLoanTypeId()));
    Flux<Application> result = repositoryAdapter.findAll();
    StepVerifier
      .create(result)
      .assertNext(app -> assertAll(() -> assertEquals(application1.getApplicationId(), app.getApplicationId()), () -> assertEquals(application1.getAmount(), app.getAmount()),
                                   () -> assertEquals(application1.getTerm(), app.getTerm()), () -> assertEquals(application1.getEmail(), app.getEmail()),
                                   () -> assertEquals(application1.getIdentityDocument(), app.getIdentityDocument()),
                                   () -> assertEquals(application1.getStatusId(), app.getStatusId()), () -> assertEquals(application1.getLoanTypeId(), app.getLoanTypeId())))
      .assertNext(app -> assertAll(() -> assertEquals(application2.getApplicationId(), app.getApplicationId()), () -> assertEquals(application2.getAmount(), app.getAmount()),
                                   () -> assertEquals(application2.getTerm(), app.getTerm()), () -> assertEquals(application2.getEmail(), app.getEmail()),
                                   () -> assertEquals(application2.getIdentityDocument(), app.getIdentityDocument()),
                                   () -> assertEquals(application2.getStatusId(), app.getStatusId()), () -> assertEquals(application2.getLoanTypeId(), app.getLoanTypeId())))
      .verifyComplete();
  }

  @Test
  void mustFindByExample() {
    UUID applicationId = UUID.randomUUID();
    ApplicationEntity applicationEntity = new ApplicationEntity(applicationId, new BigDecimal("1000.00"), "12 months", "test@example.com", 123456789L, 1L, 2L);
    Application application = Application
      .builder()
      .applicationId(applicationId)
      .amount(new BigDecimal("1000.00"))
      .term("12 months")
      .email("test@example.com")
      .identityDocument(123456789L)
      .statusId(1L)
      .loanTypeId(2L)
      .build();
    when(mapper.map(application, ApplicationEntity.class)).thenReturn(applicationEntity);
    when(repository.findAll(any(Example.class))).thenReturn(Flux.just(applicationEntity));
    when(mapper.mapBuilder(applicationEntity, Application.Builder.class)).thenReturn(Application
                                                                                       .builder()
                                                                                       .applicationId(applicationEntity.getApplicationId())
                                                                                       .amount(applicationEntity.getAmount())
                                                                                       .term(applicationEntity.getTerm())
                                                                                       .email(applicationEntity.getEmail())
                                                                                       .identityDocument(applicationEntity.getIdentityDocument())
                                                                                       .statusId(applicationEntity.getStatusId())
                                                                                       .loanTypeId(applicationEntity.getLoanTypeId()));
    Flux<Application> result = repositoryAdapter.findByExample(application);
    StepVerifier
      .create(result)
      .assertNext(app -> assertAll(() -> assertEquals(application.getApplicationId(), app.getApplicationId()), () -> assertEquals(application.getAmount(), app.getAmount()),
                                   () -> assertEquals(application.getTerm(), app.getTerm()), () -> assertEquals(application.getEmail(), app.getEmail()),
                                   () -> assertEquals(application.getIdentityDocument(), app.getIdentityDocument()),
                                   () -> assertEquals(application.getStatusId(), app.getStatusId()), () -> assertEquals(application.getLoanTypeId(), app.getLoanTypeId())))
      .verifyComplete();
  }

  @Test
  void mustSaveValue() {
    UUID applicationId = UUID.randomUUID();
    ApplicationEntity applicationEntity = new ApplicationEntity(applicationId, new BigDecimal("1000.00"), "12 months", "test@example.com", 123456789L, 1L, 2L);
    Application application = Application
      .builder()
      .applicationId(applicationId)
      .amount(new BigDecimal("1000.00"))
      .term("12 months")
      .email("test@example.com")
      .identityDocument(123456789L)
      .statusId(1L)
      .loanTypeId(2L)
      .build();
    when(mapper.map(application, ApplicationEntity.class)).thenReturn(applicationEntity);
    when(repository.save(applicationEntity)).thenReturn(Mono.just(applicationEntity));
    when(mapper.mapBuilder(applicationEntity, Application.Builder.class)).thenReturn(Application
                                                                                       .builder()
                                                                                       .applicationId(applicationEntity.getApplicationId())
                                                                                       .amount(applicationEntity.getAmount())
                                                                                       .term(applicationEntity.getTerm())
                                                                                       .email(applicationEntity.getEmail())
                                                                                       .identityDocument(applicationEntity.getIdentityDocument())
                                                                                       .statusId(applicationEntity.getStatusId())
                                                                                       .loanTypeId(applicationEntity.getLoanTypeId()));
    Mono<Application> result = repositoryAdapter.save(application);
    StepVerifier
      .create(result)
      .assertNext(app -> assertAll(() -> assertEquals(application.getApplicationId(), app.getApplicationId()), () -> assertEquals(application.getAmount(), app.getAmount()),
                                   () -> assertEquals(application.getTerm(), app.getTerm()), () -> assertEquals(application.getEmail(), app.getEmail()),
                                   () -> assertEquals(application.getIdentityDocument(), app.getIdentityDocument()),
                                   () -> assertEquals(application.getStatusId(), app.getStatusId()), () -> assertEquals(application.getLoanTypeId(), app.getLoanTypeId())))
      .verifyComplete();
  }

}