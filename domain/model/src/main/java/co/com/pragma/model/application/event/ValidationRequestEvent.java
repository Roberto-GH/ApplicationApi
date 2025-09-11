package co.com.pragma.model.application.event;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class ValidationRequestEvent {
    private String applicantEmail;
    private BigDecimal applicantBaseSalary;
    private List<ActiveLoan> activeLoans;
    private NewLoan newLoan;

    public ValidationRequestEvent() {}

    public ValidationRequestEvent(String applicantEmail, BigDecimal applicantBaseSalary, List<ActiveLoan> activeLoans, NewLoan newLoan) {
        this.applicantEmail = applicantEmail;
        this.applicantBaseSalary = applicantBaseSalary;
        this.activeLoans = activeLoans;
        this.newLoan = newLoan;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getApplicantEmail() {
        return applicantEmail;
    }

    public void setApplicantEmail(String applicantEmail) {
        this.applicantEmail = applicantEmail;
    }

    public BigDecimal getApplicantBaseSalary() {
        return applicantBaseSalary;
    }

    public void setApplicantBaseSalary(BigDecimal applicantBaseSalary) {
        this.applicantBaseSalary = applicantBaseSalary;
    }

    public List<ActiveLoan> getActiveLoans() {
        return activeLoans;
    }

    public void setActiveLoans(List<ActiveLoan> activeLoans) {
        this.activeLoans = activeLoans;
    }

    public NewLoan getNewLoan() {
        return newLoan;
    }

    public void setNewLoan(NewLoan newLoan) {
        this.newLoan = newLoan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidationRequestEvent that = (ValidationRequestEvent) o;
        return Objects.equals(applicantEmail, that.applicantEmail) && Objects.equals(applicantBaseSalary, that.applicantBaseSalary) && Objects.equals(activeLoans, that.activeLoans) && Objects.equals(newLoan, that.newLoan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicantEmail, applicantBaseSalary, activeLoans, newLoan);
    }

    public static class Builder {
        private String applicantEmail;
        private BigDecimal applicantBaseSalary;
        private List<ActiveLoan> activeLoans;
        private NewLoan newLoan;

        public Builder applicantEmail(String applicantEmail) {
            this.applicantEmail = applicantEmail;
            return this;
        }

        public Builder applicantBaseSalary(BigDecimal applicantBaseSalary) {
            this.applicantBaseSalary = applicantBaseSalary;
            return this;
        }

        public Builder activeLoans(List<ActiveLoan> activeLoans) {
            this.activeLoans = activeLoans;
            return this;
        }

        public Builder newLoan(NewLoan newLoan) {
            this.newLoan = newLoan;
            return this;
        }

        public ValidationRequestEvent build() {
            return new ValidationRequestEvent(applicantEmail, applicantBaseSalary, activeLoans, newLoan);
        }
    }
}
