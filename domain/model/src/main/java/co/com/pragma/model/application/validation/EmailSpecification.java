package co.com.pragma.model.application.validation;

import reactor.core.publisher.Mono;

import java.util.regex.Pattern;

public class EmailSpecification implements Specification<String> {

  private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
  private final String fieldName;

  public EmailSpecification(String fieldName) {
    this.fieldName = fieldName;
  }

  @Override
  public Mono<Void> validate(String candidate) {
    if (candidate == null || candidate.trim().isEmpty()) {
      return Mono.error(new DomainValidationException("The email field '" + fieldName + "' cannot be empty.", 400));
    }
    if (!EMAIL_PATTERN.matcher(candidate).matches()) {
      return Mono.error(new DomainValidationException("The format of the field '" + fieldName + "' is not a valid email.", 400));
    }
    return Mono.empty();
  }

}
