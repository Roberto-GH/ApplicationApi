package co.com.pragma.r2dbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class LoanTypeEntityTest {

  private final Long loanTypeId = 1L;
  private final String name = "Personal Loan";
  private final BigDecimal minimumAmount = new BigDecimal("1000.00");
  private final BigDecimal maximumAmount = new BigDecimal("10000.00");
  private final BigDecimal interestRate = new BigDecimal("0.05");
  private final Boolean automaticValidation = true;

  private LoanTypeEntity loanTypeEntity;

  @BeforeEach
  void setUp() {
    loanTypeEntity = new LoanTypeEntity(loanTypeId, name, minimumAmount, maximumAmount, interestRate, automaticValidation);
  }

  @Test
  void testLoanTypeEntity() {
    assertAll(
        () -> assertEquals(loanTypeId, loanTypeEntity.getLoanTypeId()),
        () -> assertEquals(name, loanTypeEntity.getName()),
        () -> assertEquals(minimumAmount, loanTypeEntity.getMinimumAmount()),
        () -> assertEquals(maximumAmount, loanTypeEntity.getMaximumAmount()),
        () -> assertEquals(interestRate, loanTypeEntity.getInterestRate()),
        () -> assertEquals(automaticValidation, loanTypeEntity.getAutomaticValidation()
      ));
  }

  @Test
  void testSetters() {
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
    assertAll(
        () -> assertEquals(newLoanTypeId, loanTypeEntity.getLoanTypeId()),
        () -> assertEquals(newName, loanTypeEntity.getName()),
        () -> assertEquals(newMinimumAmount, loanTypeEntity.getMinimumAmount()),
        () -> assertEquals(newMaximumAmount, loanTypeEntity.getMaximumAmount()),
        () -> assertEquals(newInterestRate, loanTypeEntity.getInterestRate()),
        () -> assertEquals(newAutomaticValidation, loanTypeEntity.getAutomaticValidation()
      ));
  }

  @Test
  void testNoArgsConstructor() {
    LoanTypeEntity loanTypeEntity = new LoanTypeEntity();
    assertNotNull(loanTypeEntity);
  }

}
