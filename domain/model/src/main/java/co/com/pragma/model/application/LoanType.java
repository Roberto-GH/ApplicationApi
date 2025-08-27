package co.com.pragma.model.application;

import java.math.BigDecimal;

public class LoanType {

  private final Long loanTypeId;
  private final String name;
  private final BigDecimal minimumAmount;
  private final BigDecimal maximumAmount;
  private final BigDecimal interestRate;
  private final Boolean automaticValidation;

  private LoanType(Builder builder) {
    this.loanTypeId = builder.loanTypeId;
    this.name = builder.name;
    this.minimumAmount = builder.minimumAmount;
    this.maximumAmount = builder.maximumAmount;
    this.interestRate = builder.interestRate;
    this.automaticValidation = builder.automaticValidation;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Long getLoanTypeId() {
    return loanTypeId;
  }

  public String getName() {
    return name;
  }

  public BigDecimal getMinimumAmount() {
    return minimumAmount;
  }

  public BigDecimal getMaximumAmount() {
    return maximumAmount;
  }

  public BigDecimal getInterestRate() {
    return interestRate;
  }

  public Boolean getAutomaticValidation() {
    return automaticValidation;
  }

  public static class Builder {

    private Long loanTypeId;
    private String name;
    private BigDecimal minimumAmount;
    private BigDecimal maximumAmount;
    private BigDecimal interestRate;
    private Boolean automaticValidation;

    public Builder loanTypeId(Long loanTypeId) {
      this.loanTypeId = loanTypeId;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder minimumAmount(BigDecimal minimumAmount) {
      this.minimumAmount = minimumAmount;
      return this;
    }

    public Builder maximumAmount(BigDecimal maximumAmount) {
      this.maximumAmount = maximumAmount;
      return this;
    }

    public Builder interestRate(BigDecimal interestRate) {
      this.interestRate = interestRate;
      return this;
    }

    public Builder automaticValidation(Boolean automaticValidation) {
      this.automaticValidation = automaticValidation;
      return this;
    }

    public LoanType build() {
      return new LoanType(this);
    }

  }

}
