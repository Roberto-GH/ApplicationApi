package co.com.pragma.model.application.gateways;

import co.com.pragma.model.application.MessageBody;
import reactor.core.publisher.Mono;

public interface SenderGateway {

  Mono<String> send(MessageBody message);

}
