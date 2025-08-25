package co.com.pragma.usecase.application.adapters;

import co.com.pragma.model.application.Application;
import reactor.core.publisher.Mono;

public interface ApplicationControllerUseCase {

  Mono<Application> saveApplication(Application user);

}
