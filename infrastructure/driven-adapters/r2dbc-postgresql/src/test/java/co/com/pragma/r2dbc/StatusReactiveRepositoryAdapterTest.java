package co.com.pragma.r2dbc;

import co.com.pragma.model.application.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatusReactiveRepositoryAdapterTest {

  @Mock
  private StatusReactiveRepository repository;
  @Mock
  private ObjectMapper mapper;

  @InjectMocks
  private StatusReactiveRepositoryAdapter adapter;

  @Test
  void findByIdShouldReturnStatus() {
    Long statusId = 1L;
    StatusEntity statusEntity = new StatusEntity(statusId, "Pending", "Application is pending review");
    Status status = Status.builder().statusId(statusId).name("Pending").description("Application is pending review").build();
    when(repository.findById(statusId)).thenReturn(Mono.just(statusEntity));
    when(mapper.mapBuilder(statusEntity, Status.Builder.class)).thenReturn(
      Status.builder().statusId(statusEntity.getStatusId()).name(statusEntity.getName()).description(statusEntity.getDescription()));
    Mono<Status> result = adapter.findById(statusId);
    StepVerifier
      .create(result)
      .assertNext(st -> assertAll(
        () -> assertEquals(status.getStatusId(), st.getStatusId()), () -> assertEquals(status.getName(), st.getName()),
        () -> assertEquals(status.getDescription(), st.getDescription())
      ))
      .verifyComplete();
    verify(repository, times(1)).findById(statusId);
    verify(mapper, times(1)).mapBuilder(statusEntity, Status.Builder.class);
  }

  @Test
  void findByIdShouldReturnEmptyMonoWhenNotFound() {
    Long statusId = 1L;
    when(repository.findById(statusId)).thenReturn(Mono.empty());
    Mono<Status> result = adapter.findById(statusId);
    StepVerifier.create(result).expectComplete().verify();
    verify(repository, times(1)).findById(statusId);
    verify(mapper, never()).mapBuilder(any(), any());
  }

}
