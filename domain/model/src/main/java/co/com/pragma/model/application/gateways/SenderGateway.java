package co.com.pragma.model.application.gateways;

import co.com.pragma.model.application.enums.QueueAlias;
import reactor.core.publisher.Mono;

public interface SenderGateway {

  <T> Mono<String> send(T messageObject, QueueAlias destination);

}
