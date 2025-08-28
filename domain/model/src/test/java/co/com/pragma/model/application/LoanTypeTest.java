package co.com.pragma.model.application;

import org.junit.jupiter.api.*;

import java.math.BigDecimal;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LoanTypeTest {

  private static LoanType loanType;

  @BeforeAll
  static void setUpAll() {
    loanType = LoanType
      .builder()
      .loanTypeId(345345L)
      .name("name")
      .minimumAmount(BigDecimal.TEN)
      .maximumAmount(BigDecimal.TWO)
      .interestRate(BigDecimal.ONE)
      .automaticValidation(true)
      .build();
  }

  @Test
  void getLoanTypeId() {
    Assertions.assertEquals(345345L, loanType.getLoanTypeId());
  }

  @Test
  void getName() {
    Assertions.assertEquals("name", loanType.getName());
  }

  @Test
  void getMinimumAmount() {
    Assertions.assertEquals(BigDecimal.TEN, loanType.getMinimumAmount());
  }

  @Test
  void getMaximumAmount() {
    Assertions.assertEquals(BigDecimal.TWO, loanType.getMaximumAmount());
  }

  @Test
  void getInterestRate() {
    Assertions.assertEquals(BigDecimal.ONE, loanType.getInterestRate());
  }

  @Test
  void getAutomaticValidation() {
    Assertions.assertEquals(true, loanType.getAutomaticValidation());
  }

}