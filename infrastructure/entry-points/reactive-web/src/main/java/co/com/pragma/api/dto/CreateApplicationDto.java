package co.com.pragma.api.dto;

import java.math.BigDecimal;

public record CreateApplicationDto (
  BigDecimal amount,
  Integer term,
  String email,
  Long identityDocument,
  Long loanTypeId
) {}
