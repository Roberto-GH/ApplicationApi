package co.com.pragma.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

public record ApplicationDataDto(
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
