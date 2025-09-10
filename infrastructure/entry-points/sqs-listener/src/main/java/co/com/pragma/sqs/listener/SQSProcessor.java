package co.com.pragma.sqs.listener;

import co.com.pragma.model.application.exception.DomainValidationException;
import co.com.pragma.model.application.exception.ErrorEnum;
import co.com.pragma.sqs.listener.dto.LoanValidatedDto;
import co.com.pragma.usecase.application.adapters.ApplicationControllerUseCase;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.function.Function;

@Service
public class SQSProcessor implements Function<Message, Mono<Void>> {

  private static final Logger LOG = LoggerFactory.getLogger(SQSProcessor.class);
  private static final Gson gson = new Gson();

  private final ApplicationControllerUseCase applicationControllerUseCase;

  public SQSProcessor(ApplicationControllerUseCase applicationControllerUseCase) {
    this.applicationControllerUseCase = applicationControllerUseCase;
  }

  @Override
  public Mono<Void> apply(Message message) {
    LoanValidatedDto loan = gson.fromJson(message.body(), LoanValidatedDto.class);
    LOG.info("Application loan validated: {}", gson.toJson(loan));
    return Mono.just(loan)
      .flatMap(dto -> {
        if (dto.getLoanId() == null || dto.getLoanId().isBlank() || dto.getStatusId() == null){
          return Mono.error(new DomainValidationException(ErrorEnum.INVALID_APPLICATION_DATA, "Loan validated data error"));
        }
        return Mono.just(dto);
      })
      .flatMap(dto -> applicationControllerUseCase.getApplicationById(dto.getLoanId())
        .switchIfEmpty(Mono.error(new DomainValidationException(ErrorEnum.INVALID_APPLICATION_DATA, "Application loan no exist")))
        .map(app -> {
          app.setStatusId(dto.getStatusId());
          return app;
        }).doOnNext(a -> LOG.info("Loan actualizado => {}", gson.toJson(a)))
        .flatMap(applicationControllerUseCase::patchApplicationStatus)
        .flatMap(reponseUser -> Mono.empty())
      );
  }

}
