package co.com.pragma.model.application;

import java.math.BigDecimal;
import java.util.UUID;

public class Application {

  private final UUID applicationId;
  private final BigDecimal amount;
  private final String term;
  private final String email;
  private final Long identityDocument;
  private final Long statusId;
  private final Long loanTypeId;

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

  public BigDecimal getAmount() {
    return amount;
  }

  public String getTerm() {
    return term;
  }

  public String getEmail() {
    return email;
  }

  public Long getIdentityDocument() {
    return identityDocument;
  }

  public Long getStatusId() {
    return statusId;
  }

  public Long getLoanTypeId() {
    return loanTypeId;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private UUID applicationId;
    private BigDecimal amount;
    private String term;
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

    public Builder term(String term) {
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
      return new Application(this);
    }

  }

}