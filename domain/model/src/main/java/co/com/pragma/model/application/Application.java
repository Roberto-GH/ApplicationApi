package co.com.pragma.model.application;

import java.math.BigDecimal;
import java.util.UUID;

public class Application {

  private UUID applicationId;
  private BigDecimal amount;
  private Integer term;
  private String email;
  private Long identityDocument;
  private Long statusId;
  private Long loanTypeId;

  public Application() {
  }

  private Application(Builder builder) {
    this.applicationId = builder.applicationId;
    this.amount = builder.amount;
    this.term = builder.term;
    this.email = builder.email;
    this.identityDocument = builder.identityDocument;
    this.statusId = builder.statusId;
    this.loanTypeId = builder.loanTypeId;
  }

  public UUID getApplicationId() {
    return applicationId;
  }

  public void setApplicationId(UUID applicationId) {
    this.applicationId = applicationId;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public Integer getTerm() {
    return term;
  }

  public void setTerm(Integer term) {
    this.term = term;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Long getIdentityDocument() {
    return identityDocument;
  }

  public void setIdentityDocument(Long identityDocument) {
    this.identityDocument = identityDocument;
  }

  public Long getStatusId() {
    return statusId;
  }

  public void setStatusId(Long statusId) {
    this.statusId = statusId;
  }

  public Long getLoanTypeId() {
    return loanTypeId;
  }

  public void setLoanTypeId(Long loanTypeId) {
    this.loanTypeId = loanTypeId;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private UUID applicationId;
    private BigDecimal amount;
    private Integer term;
    private String email;
    private Long identityDocument;
    private Long statusId;
    private Long loanTypeId;

    public Builder applicationId(UUID applicationId) {
      this.applicationId = applicationId;
      return this;
    }

    public Builder amount(BigDecimal amount) {
      this.amount = amount;
      return this;
    }

    public Builder term(Integer term) {
      this.term = term;
      return this;
    }

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    public Builder identityDocument(Long identityDocument) {
      this.identityDocument = identityDocument;
      return this;
    }

    public Builder statusId(Long statusId) {
      this.statusId = statusId;
      return this;
    }

    public Builder loanTypeId(Long loanTypeId) {
      this.loanTypeId = loanTypeId;
      return this;
    }

    public Application build() {
      if(this.loanTypeId == null || this.loanTypeId <= 0){
        this.loanTypeId = 1L;
      }
      if(this.statusId == null || this.statusId <= 0){
        this.statusId = 1L;
      }
      return new Application(this);
    }

  }

}
