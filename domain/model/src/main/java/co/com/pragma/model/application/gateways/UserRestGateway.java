package co.com.pragma.model.application.gateways;

import co.com.pragma.model.application.User;
import reactor.core.publisher.Mono;

public interface UserRestGateway {

  Mono<User> findUserByEmail(String mail);

}
