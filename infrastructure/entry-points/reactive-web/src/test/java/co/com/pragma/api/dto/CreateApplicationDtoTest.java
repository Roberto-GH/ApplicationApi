package co.com.pragma.api.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateApplicationDtoTest {

  @Test
  void testCreateApplicationDto() {
    BigDecimal amount = new BigDecimal("1500.00");
    Integer term = 18; // Changed from String to Integer
    String email = "create@example.com";
    Long identityDocument = 987654321L;
    Long loanTypeId = 3L;
    CreateApplicationDto dto = new CreateApplicationDto(amount, term, email, identityDocument, loanTypeId);
    assertAll(
      () -> assertEquals(amount, dto.amount()),
      () -> assertEquals(term, dto.term()),
      () -> assertEquals(email, dto.email()),
      () -> assertEquals(identityDocument, dto.identityDocument()),
      () -> assertEquals(loanTypeId, dto.loanTypeId())
    );
  }

}