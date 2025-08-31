package co.com.pragma.api.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ApplicationResponseDto(
  BigDecimal amount,
  String term,
  String email,
  Long identityDocument,
  Long statusId,
  Long loanTypeId
) {}
