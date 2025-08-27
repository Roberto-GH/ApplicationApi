package co.com.pragma.model.application.validation;

import reactor.core.publisher.Mono;

/**
 * @param <T> El tipo de objeto a validar.
 */
@FunctionalInterface
public interface Specification<T> {

  /**
   * Valida el candidato contra la especificaci�n.
   * Lanza una excepci�n si la regla no se cumple.
   */
  Mono<Void> validate(T candidate);

}
