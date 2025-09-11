package co.com.pragma.model.application.event;

import java.math.BigDecimal;
import java.util.Objects;

public class ActiveLoan {
    private String loanId;
    private BigDecimal requestedAmount;
    private BigDecimal monthlyInterestRate;
    private Integer requestedTermMonths;

    public ActiveLoan() {}

    public ActiveLoan(String loanId, BigDecimal requestedAmount, BigDecimal monthlyInterestRate, Integer requestedTermMonths) {
        this.loanId = loanId;
        this.requestedAmount = requestedAmount;
        this.monthlyInterestRate = monthlyInterestRate;
        this.requestedTermMonths = requestedTermMonths;
    }
    
    public static ActiveLoanBuilder builder() {
        return new ActiveLoanBuilder();
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
        ActiveLoan that = (ActiveLoan) o;
        return Objects.equals(loanId, that.loanId) && Objects.equals(requestedAmount, that.requestedAmount) && Objects.equals(monthlyInterestRate, that.monthlyInterestRate) && Objects.equals(requestedTermMonths, that.requestedTermMonths);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loanId, requestedAmount, monthlyInterestRate, requestedTermMonths);
    }
    
    public static class ActiveLoanBuilder {
        private String loanId;
        private BigDecimal requestedAmount;
        private BigDecimal monthlyInterestRate;
        private Integer requestedTermMonths;

        public ActiveLoanBuilder loanId(String loanId) { this.loanId = loanId; return this; }
        public ActiveLoanBuilder requestedAmount(BigDecimal requestedAmount) { this.requestedAmount = requestedAmount; return this; }
        public ActiveLoanBuilder monthlyInterestRate(BigDecimal monthlyInterestRate) { this.monthlyInterestRate = monthlyInterestRate; return this; }
        public ActiveLoanBuilder requestedTermMonths(Integer requestedTermMonths) { this.requestedTermMonths = requestedTermMonths; return this; }

        public ActiveLoan build() {
            return new ActiveLoan(loanId, requestedAmount, monthlyInterestRate, requestedTermMonths);
        }
    }
}
