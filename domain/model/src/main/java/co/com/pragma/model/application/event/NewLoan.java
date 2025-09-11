package co.com.pragma.model.application.event;

import java.math.BigDecimal;
import java.util.Objects;

public class NewLoan {
    private String loanId;
    private BigDecimal requestedAmount;
    private BigDecimal monthlyInterestRate;
    private Integer requestedTermMonths;

    public NewLoan() {}

    public NewLoan(String loanId, BigDecimal requestedAmount, BigDecimal monthlyInterestRate, Integer requestedTermMonths) {
        this.loanId = loanId;
        this.requestedAmount = requestedAmount;
        this.monthlyInterestRate = monthlyInterestRate;
        this.requestedTermMonths = requestedTermMonths;
    }
    
    public static NewLoanBuilder builder() {
        return new NewLoanBuilder();
    }

    public String getLoanId() { return loanId; }
    public void setLoanId(String loanId) { this.loanId = loanId; }
    public BigDecimal getRequestedAmount() { return requestedAmount; }
    public void setRequestedAmount(BigDecimal requestedAmount) { this.requestedAmount = requestedAmount; }
    public BigDecimal getMonthlyInterestRate() { return monthlyInterestRate; }
    public void setMonthlyInterestRate(BigDecimal monthlyInterestRate) { this.monthlyInterestRate = monthlyInterestRate; }
    public Integer getRequestedTermMonths() { return requestedTermMonths; }
    public void setRequestedTermMonths(Integer requestedTermMonths) { this.requestedTermMonths = requestedTermMonths; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewLoan that = (NewLoan) o;
        return Objects.equals(loanId, that.loanId) && Objects.equals(requestedAmount, that.requestedAmount) && Objects.equals(monthlyInterestRate, that.monthlyInterestRate) && Objects.equals(requestedTermMonths, that.requestedTermMonths);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loanId, requestedAmount, monthlyInterestRate, requestedTermMonths);
    }
    
    public static class NewLoanBuilder {
        private String loanId;
        private BigDecimal requestedAmount;
        private BigDecimal monthlyInterestRate;
        private Integer requestedTermMonths;

        public NewLoanBuilder loanId(String loanId) { this.loanId = loanId; return this; }
        public NewLoanBuilder requestedAmount(BigDecimal requestedAmount) { this.requestedAmount = requestedAmount; return this; }
        public NewLoanBuilder monthlyInterestRate(BigDecimal monthlyInterestRate) { this.monthlyInterestRate = monthlyInterestRate; return this; }
        public NewLoanBuilder requestedTermMonths(Integer requestedTermMonths) { this.requestedTermMonths = requestedTermMonths; return this; }

        public NewLoan build() {
            return new NewLoan(loanId, requestedAmount, monthlyInterestRate, requestedTermMonths);
        }
    }
}
