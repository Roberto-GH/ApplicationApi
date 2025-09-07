package co.com.pragma.model.application.gateways;

import co.com.pragma.model.application.SqsMessageBody;
import reactor.core.publisher.Mono;

public interface SQSSenderGateway {

  Mono<String> send(SqsMessageBody message);

}
