package co.com.pragma.model.application;

import java.util.List;

public class MessageBody {

  private final String email;
  private final String subject;
  private final String message;
  private final List<PaymentPlan> paymentPlan;

  private MessageBody(Builder builder) {
    this.email = builder.email;
    this.subject = builder.subject;
    this.message = builder.message;
    this.paymentPlan = builder.paymentPlan;
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

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private String email;
    private String subject;
    private String message;
    private List<PaymentPlan> paymentPlan;

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

    public MessageBody build() {
      return new MessageBody(this);
    }

  }

}
