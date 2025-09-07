package co.com.pragma.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.UUID;

public record ApplicationDataDto(
  UUID id,
  BigDecimal amount,
  Integer term,
  String email,
  String name,
  String loanType,
  BigDecimal interestRate,
  String applicationStatus,
  BigDecimal baseSalary,
  @JsonInclude(JsonInclude.Include.NON_NULL)
  BigDecimal totalMonthlyPayment
) {}
