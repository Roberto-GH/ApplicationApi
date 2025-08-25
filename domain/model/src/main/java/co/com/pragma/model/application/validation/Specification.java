package co.com.pragma.model.application.validation;

/**
 * @param <T> El tipo de objeto a validar.
 */
@FunctionalInterface
public interface Specification<T> {

  /**
   * Valida el candidato contra la especificación.
   * Lanza una excepción si la regla no se cumple.
   */
  void validate(T candidate) throws DomainValidationException;

}
