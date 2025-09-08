package co.com.pragma.api;

import co.com.pragma.api.constants.ApplicationWebKeys;
import co.com.pragma.api.dto.CreateApplicationDto;
import co.com.pragma.api.exception.ApplicationApiException;
import co.com.pragma.api.jwt.JwtProvider;
import co.com.pragma.api.mapper.ApplicationDtoMapper;
import co.com.pragma.model.application.Application;
import co.com.pragma.model.application.PathApplication;
import co.com.pragma.model.application.exception.ErrorEnum;
import co.com.pragma.usecase.application.adapters.ApplicationControllerUseCase;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class Handler {

  private static final Logger LOG = LoggerFactory.getLogger(Handler.class);
  private static final Gson gson = new Gson();
  private final ApplicationControllerUseCase applicationControllerUseCase;
  private final ApplicationDtoMapper applicationDtoMapper;
  private final TransactionalOperator transactionalOperator;
  private final JwtProvider jwtProvider;

  public Handler(ApplicationControllerUseCase applicationControllerUseCase, ApplicationDtoMapper applicationDtoMapper, TransactionalOperator transactionalOperator,
                 JwtProvider jwtProvider) {
    this.applicationControllerUseCase = applicationControllerUseCase;
    this.applicationDtoMapper = applicationDtoMapper;
    this.transactionalOperator = transactionalOperator;
    this.jwtProvider = jwtProvider;
  }

  @PreAuthorize("hasRole('USER')")
  public Mono<ServerResponse> listenSaveApplication(ServerRequest serverRequest) {
    return Mono.justOrEmpty(serverRequest.headers().firstHeader(ApplicationWebKeys.HEADER_AUTHORIZATION))
      .switchIfEmpty(Mono.error(new ApplicationApiException(ErrorEnum.INVALID_TOKEN, ApplicationWebKeys.HEADER_MISSING)))
      .flatMap(header -> {
        String token = header.replace(ApplicationWebKeys.BEARER, ApplicationWebKeys.STRING_BLANK);
        Claims claims = jwtProvider.getClaims(token);
        String requestUserEmail = claims.getSubject();
        LOG.debug(requestUserEmail);
        return Mono.just(requestUserEmail);
      }).flatMap(emailSolicitante -> serverRequest
        .bodyToMono(CreateApplicationDto.class)
        .switchIfEmpty(Mono.error(new ApplicationApiException(ErrorEnum.INVALID_APPLICATION_DATA, ApplicationWebKeys.ERROR_DATA_REQUIRED)))
        .filter(dto -> dto.email().equals(emailSolicitante))
        .switchIfEmpty(Mono.error(new ApplicationApiException(ErrorEnum.INVALID_APPLICATION_DATA, ApplicationWebKeys.EMAIL_NOT_MATCH)))
        .map(dto -> {
          Application.Builder userBuilder = applicationDtoMapper.toModel(dto);
          return userBuilder.build();
        })
        .flatMap(application -> applicationControllerUseCase.saveApplication(application).as(transactionalOperator::transactional))
        .map(applicationDtoMapper::toResponseDto)
        .flatMap(reponseUser -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(reponseUser)));
  }

  @PreAuthorize("hasRole('ADVISOR')")
  public Mono<ServerResponse> listenGetApplications(ServerRequest serverRequest) {
    return Mono.just(serverRequest)
      .flatMap(req -> Mono.justOrEmpty(req.queryParam(ApplicationWebKeys.PARAM_PAGE_SIZE))
        .switchIfEmpty(Mono.error(new ApplicationApiException(ErrorEnum.INVALID_APPLICATION_DATA, ApplicationWebKeys.ERROR_PAGE_SIZE)))
        .map(Integer::parseInt)
        .flatMap(pageSize -> Mono.justOrEmpty(req.queryParam(ApplicationWebKeys.PARAM_PAGE_NUMBER))
          .switchIfEmpty(Mono.error(new ApplicationApiException(ErrorEnum.INVALID_APPLICATION_DATA, ApplicationWebKeys.ERROR_PAGE_NUMBER)))
          .map(Integer::parseInt)
          .flatMap(pageNumber -> {
            Integer status = req.queryParam("status").map(Integer::parseInt).orElse(null);
            Integer loanType = req.queryParam("loanType").map(Integer::parseInt).orElse(null);
            return applicationControllerUseCase.getApplicationsByStatusAndLoanType(status, loanType, pageSize, pageNumber);
          })
        )
      )
      .flatMap(paginationResponse -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
        .bodyValue(applicationDtoMapper.toApplicationListDto(paginationResponse)));
  }

  @PreAuthorize("hasRole('ADVISOR')")
  public Mono<ServerResponse> listenPatchApplication(ServerRequest serverRequest) {
    return serverRequest
      .bodyToMono(PathApplication.class)
      .switchIfEmpty(Mono.error(new ApplicationApiException(ErrorEnum.INVALID_APPLICATION_DATA, ApplicationWebKeys.ERROR_DATA_REQUIRED)))
      .doOnNext(dto -> LOG.info("ApplicationDto patch recibido => {}", gson.toJson(dto)))
      .flatMap(dto -> {
        if (dto.getApplicationId() == null || dto.getApplicationId().isBlank() || dto.getStatusId() == null){
          return Mono.error(new ApplicationApiException(ErrorEnum.INVALID_APPLICATION_DATA, ApplicationWebKeys.INVALIDA_PATCH_APPLICATION_REQUEST));
        }
        return Mono.just(dto);
      })
      .flatMap(dto -> applicationControllerUseCase.getApplicationById(dto.getApplicationId())
      .switchIfEmpty(Mono.error(new ApplicationApiException(ErrorEnum.INVALID_APPLICATION_DATA, ApplicationWebKeys.APPLICATION_NOT_EXIST)))
      .map(app -> {
        app.setStatusId(dto.getStatusId());
        return app;
      }).doOnNext(a -> LOG.info("Application actualizado => {}", gson.toJson(a)))
      .flatMap(application -> applicationControllerUseCase.patchApplicationStatus(application).as(transactionalOperator::transactional))
      .map(applicationDtoMapper::toResponseDto)
      .flatMap(reponseUser -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(reponseUser)));
  }

}
