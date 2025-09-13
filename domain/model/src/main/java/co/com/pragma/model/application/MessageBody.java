package co.com.pragma.model.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class MessageBody {

  private final UUID applicationLoanId;
  private final String email;
  private final String subject;
  private final String message;
  private final Integer status;
  private final BigDecimal amount;
  private final List<PaymentPlan> paymentPlan;

  private MessageBody(Builder builder) {
    this.applicationLoanId = builder.applicationLoanId;
    this.email = builder.email;
    this.subject = builder.subject;
    this.message = builder.message;
    this.status = builder.status;
    this.amount = builder.amount;
    this.paymentPlan = builder.paymentPlan;
  }

  public UUID getApplicationLoanId() {
    return applicationLoanId;
  }

  public String getEmail() {
    return email;
  }

  public String getSubject() {
    return subject;
  }

  public String getMessage() {
    return message;
  }

  public List<PaymentPlan> getPaymentPlan() {
    return paymentPlan;
  }

  public Integer getStatus() {
    return status;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private UUID applicationLoanId;
    private String email;
    private String subject;
    private String message;
    private Integer status;
    private BigDecimal amount;
    private List<PaymentPlan> paymentPlan;

    public Builder applicationLoanId(UUID applicationLoanId) {
      this.applicationLoanId = applicationLoanId;
      return this;
    }

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    public Builder subject(String subject) {
      this.subject = subject;
      return this;
    }

    public Builder message(String message) {
      this.message = message;
      return this;
    }

    public Builder paymentPlan(List<PaymentPlan> paymentPlan) {
      this.paymentPlan = paymentPlan;
      return this;
    }

    public Builder status(Integer status) {
      this.status = status;
      return this;
    }

    public Builder amount(BigDecimal amount) {
      this.amount = amount;
      return this;
    }

    public MessageBody build() {
      return new MessageBody(this);
    }

  }

}
