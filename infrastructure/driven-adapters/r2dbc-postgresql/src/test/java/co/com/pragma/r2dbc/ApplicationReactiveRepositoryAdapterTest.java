package co.com.pragma.r2dbc;

import co.com.pragma.model.application.Application;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationReactiveRepositoryAdapterTest {

  private final UUID applicationId = UUID.randomUUID();
  private ApplicationEntity applicationEntity;
  private Application application;
  private Application.Builder builder;
  private final UUID applicationId2 = UUID.randomUUID();
  private ApplicationEntity applicationEntity2;
  private Application application2;

  @Mock
  private ApplicationReactiveRepository repository;
  @Mock
  private ObjectMapper mapper;

  @InjectMocks
  private ApplicationReactiveRepositoryAdapter repositoryAdapter;

  @BeforeEach
  void setUp() {
    applicationEntity = new ApplicationEntity();
    applicationEntity.setApplicationId(applicationId);
    application = Application
      .builder()
      .applicationId(applicationId)
      .build();
    builder = Application
      .builder()
      .applicationId(applicationEntity.getApplicationId());
    applicationEntity2 = new ApplicationEntity();
    applicationEntity2.setApplicationId(applicationId2);
    application2 = Application.builder().applicationId(applicationId2).build();
  }

  @Test
  void mustFindValueById() {
    when(repository.findById(applicationId)).thenReturn(Mono.just(applicationEntity));
    when(mapper.mapBuilder(applicationEntity, Application.Builder.class)).thenReturn(builder);
    Mono<Application> result = repositoryAdapter.findById(applicationId);
    StepVerifier.create(result)
      .assertNext(app -> assertAll(
        () -> assertEquals(application.getApplicationId(), app.getApplicationId())
      ))
      .verifyComplete();
  }

  @Test
  void mustFindAllValues() {
    when(repository.findAll()).thenReturn(Flux.just(applicationEntity, applicationEntity2));
    when(mapper.mapBuilder(applicationEntity, Application.Builder.class)).thenReturn(Application.builder().applicationId(applicationEntity.getApplicationId()));
    when(mapper.mapBuilder(applicationEntity2, Application.Builder.class)).thenReturn(Application.builder().applicationId(applicationEntity2.getApplicationId()));
    Flux<Application> result = repositoryAdapter.findAll();
    StepVerifier.create(result)
      .assertNext(
        app -> assertAll(
          () -> assertEquals(application.getApplicationId(), app.getApplicationId())))
      .assertNext(
        app -> assertAll(
          () -> assertEquals(application2.getApplicationId(), app.getApplicationId())))
      .verifyComplete();
  }

  @Test
  void mustFindByExample() {
    when(mapper.map(application, ApplicationEntity.class)).thenReturn(applicationEntity);
    when(repository.findAll(any(Example.class))).thenReturn(Flux.just(applicationEntity));
    when(mapper.mapBuilder(applicationEntity, Application.Builder.class)).thenReturn(Application.builder().applicationId(applicationEntity.getApplicationId()));
    Flux<Application> result = repositoryAdapter.findByExample(application);
    StepVerifier.create(result)
      .assertNext(app -> assertAll(
        () -> assertEquals(application.getApplicationId(), app.getApplicationId())
       ))
      .verifyComplete();
  }

  @Test
  void mustSaveValue() {
    when(mapper.map(application, ApplicationEntity.class)).thenReturn(applicationEntity);
    when(repository.save(applicationEntity)).thenReturn(Mono.just(applicationEntity));
    when(mapper.mapBuilder(applicationEntity, Application.Builder.class)).thenReturn(Application.builder().applicationId(applicationEntity.getApplicationId()));
    Mono<Application> result = repositoryAdapter.save(application);
    StepVerifier.create(result)
      .assertNext(app -> assertAll(
        () -> assertEquals(application.getApplicationId(), app.getApplicationId())
      ))
      .verifyComplete();
  }

}
