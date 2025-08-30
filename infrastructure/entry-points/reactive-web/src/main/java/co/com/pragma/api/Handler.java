package co.com.pragma.api;

import co.com.pragma.api.constants.ApplicationWebKeys;
import co.com.pragma.api.dto.CreateApplicationDto;
import co.com.pragma.api.exception.ApplicationApiException;
import co.com.pragma.api.mapper.ApplicationDtoMapper;
import co.com.pragma.model.application.Application;
import co.com.pragma.model.application.exception.ErrorEnum;
import co.com.pragma.usecase.application.adapters.ApplicationControllerUseCase;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class Handler {

  private final ApplicationControllerUseCase applicationControllerUseCase;
  private final ApplicationDtoMapper applicationDtoMapper;
  private final TransactionalOperator transactionalOperator;

  public Handler(ApplicationControllerUseCase applicationControllerUseCase, ApplicationDtoMapper applicationDtoMapper, TransactionalOperator transactionalOperator) {
    this.applicationControllerUseCase = applicationControllerUseCase;
    this.applicationDtoMapper = applicationDtoMapper;
    this.transactionalOperator = transactionalOperator;
  }

  @PreAuthorize("hasRole('USER')")
  public Mono<ServerResponse> listenSaveApplication(ServerRequest serverRequest) {
    return serverRequest
      .bodyToMono(CreateApplicationDto.class)
      .switchIfEmpty(Mono.error(new ApplicationApiException(ErrorEnum.INVALID_APPLICATION_DATA, ApplicationWebKeys.ERROR_DATA_REQUIRED)))
      .map(dto -> {
        Application.Builder userBuilder = applicationDtoMapper.toModel(dto);
        return userBuilder.build();
      })
      .flatMap(application -> applicationControllerUseCase.saveApplication(application).as(transactionalOperator::transactional))
      .map(applicationDtoMapper::toResponseDto)
      .flatMap(reponseUser -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(reponseUser));
  }

}
