package co.com.pragma.api.dto;

import java.math.BigDecimal;

public record ApplicationResponseDto(
  BigDecimal amount,
  String term,
  String email,
  Long identityDocument,
  Long statusId,
  Long loanTypeId
) {}
