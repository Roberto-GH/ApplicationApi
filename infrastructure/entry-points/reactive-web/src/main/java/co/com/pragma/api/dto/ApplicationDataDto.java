package co.com.pragma.api.dto;

import java.math.BigDecimal;

public record ApplicationDataDto(
  BigDecimal amount,
  String term,
  String email,
  String name,
  String loanType,
  BigDecimal interestRate,
  String applicationStatus,
  BigDecimal baseSalary
) {}
