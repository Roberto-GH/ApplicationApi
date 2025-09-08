package co.com.pragma.model.application;

import java.math.BigDecimal;
import java.util.UUID;

public class ApplicationData {

  private UUID id;
  private BigDecimal amount;
  private Integer term;
  private String email;
  private String loanType;
  private BigDecimal interestRate;
  private String applicationStatus;
  private Long statusId;
  private String name;
  private Long baseSalary;
  private BigDecimal totalMonthlyPayment;

  public ApplicationData() {
  }

  public ApplicationData(UUID id, BigDecimal amount, Integer term, String email, String loanType, BigDecimal interestRate, String applicationStatus, Long statusId,
                         String name, Long baseSalary, BigDecimal totalMonthlyPayment) {
    this.id = id;
    this.amount = amount;
    this.term = term;
    this.email = email;
    this.loanType = loanType;
    this.interestRate = interestRate;
    this.applicationStatus = applicationStatus;
    this.statusId = statusId;
    this.name = name;
    this.baseSalary = baseSalary;
    this.totalMonthlyPayment = totalMonthlyPayment;
  }

  public UUID getId() {
    return id;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public Integer getTerm() {
    return term;
  }

  public String getEmail() {
    return email;
  }

  public String getLoanType() {
    return loanType;
  }

  public BigDecimal getInterestRate() {
    return interestRate;
  }

  public String getApplicationStatus() {
    return applicationStatus;
  }

  public Long getStatusId() {
    return statusId;
  }

  public String getName() {
    return name;
  }

  public Long getBaseSalary() {
    return baseSalary;
  }

  public BigDecimal getTotalMonthlyPayment() {
    return totalMonthlyPayment;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public void setTerm(Integer term) {
    this.term = term;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setLoanType(String loanType) {
    this.loanType = loanType;
  }

  public void setInterestRate(BigDecimal interestRate) {
    this.interestRate = interestRate;
  }

  public void setApplicationStatus(String applicationStatus) {
    this.applicationStatus = applicationStatus;
  }

  public void setStatusId(Long statusId) {
    this.statusId = statusId;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setBaseSalary(Long baseSalary) {
    this.baseSalary = baseSalary;
  }

  public void setTotalMonthlyPayment(BigDecimal totalMonthlyPayment) {
    this.totalMonthlyPayment = totalMonthlyPayment;
  }

}