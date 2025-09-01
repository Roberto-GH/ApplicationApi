package co.com.pragma.model.application.validation;

import reactor.core.publisher.Mono;

/**
 * @param <T> The type of object to validate.
 */
@FunctionalInterface
public interface Specification<T> {

  /**
   * Validates the candidate against the specification.
   * Throws an exception if the rule is not met.
   */
  Mono<Void> validate(T candidate);

}
