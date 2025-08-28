package co.com.pragma.r2dbc;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class LoanTypeEntityTest {

  @Test
  void testLoanTypeEntity() {
    Long loanTypeId = 1L;
    String name = "Personal Loan";
    BigDecimal minimumAmount = new BigDecimal("1000.00");
    BigDecimal maximumAmount = new BigDecimal("10000.00");
    BigDecimal interestRate = new BigDecimal("0.05");
    Boolean automaticValidation = true;
    LoanTypeEntity loanTypeEntity = new LoanTypeEntity(loanTypeId, name, minimumAmount, maximumAmount, interestRate, automaticValidation);
    assertAll(() -> assertEquals(loanTypeId, loanTypeEntity.getLoanTypeId()), () -> assertEquals(name, loanTypeEntity.getName()),
              () -> assertEquals(minimumAmount, loanTypeEntity.getMinimumAmount()), () -> assertEquals(maximumAmount, loanTypeEntity.getMaximumAmount()),
              () -> assertEquals(interestRate, loanTypeEntity.getInterestRate()), () -> assertEquals(automaticValidation, loanTypeEntity.getAutomaticValidation()));
    // Test setters
    Long newLoanTypeId = 2L;
    String newName = "Auto Loan";
    BigDecimal newMinimumAmount = new BigDecimal("5000.00");
    BigDecimal newMaximumAmount = new BigDecimal("50000.00");
    BigDecimal newInterestRate = new BigDecimal("0.03");
    Boolean newAutomaticValidation = false;
    loanTypeEntity.setLoanTypeId(newLoanTypeId);
    loanTypeEntity.setName(newName);
    loanTypeEntity.setMinimumAmount(newMinimumAmount);
    loanTypeEntity.setMaximumAmount(newMaximumAmount);
    loanTypeEntity.setInterestRate(newInterestRate);
    loanTypeEntity.setAutomaticValidation(newAutomaticValidation);
    assertAll(() -> assertEquals(newLoanTypeId, loanTypeEntity.getLoanTypeId()), () -> assertEquals(newName, loanTypeEntity.getName()),
              () -> assertEquals(newMinimumAmount, loanTypeEntity.getMinimumAmount()), () -> assertEquals(newMaximumAmount, loanTypeEntity.getMaximumAmount()),
              () -> assertEquals(newInterestRate, loanTypeEntity.getInterestRate()), () -> assertEquals(newAutomaticValidation, loanTypeEntity.getAutomaticValidation()));
  }

  @Test
  void testNoArgsConstructor() {
    LoanTypeEntity loanTypeEntity = new LoanTypeEntity();
    assertNotNull(loanTypeEntity);
  }

}
