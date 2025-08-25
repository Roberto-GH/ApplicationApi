package co.com.pragma.api.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ApplicationResponseDto(
  UUID applicationId,
  BigDecimal amount,
  String term,
  String email,
  Long identityDocument,
  Long statusId,
  Long loanTypeId
) {}
