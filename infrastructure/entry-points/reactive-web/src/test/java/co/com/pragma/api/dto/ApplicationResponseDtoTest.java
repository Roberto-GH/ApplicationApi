package co.com.pragma.api.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ApplicationResponseDtoTest {

  @Test
  void testApplicationResponseDto() {
    UUID applicationId = UUID.randomUUID();
    BigDecimal amount = new BigDecimal("1000.00");
    Integer term = 12;
    String email = "test@example.com";
    Long identityDocument = 123456789L;
    Long statusId = 1L;
    Long loanTypeId = 2L;

    ApplicationResponseDto dto = new ApplicationResponseDto(
      amount, term, email, identityDocument, statusId, loanTypeId
    );
    assertAll(
      () -> assertEquals(amount, dto.amount()),
      () -> assertEquals(term, dto.term()),
      () -> assertEquals(email, dto.email()),
      () -> assertEquals(identityDocument, dto.identityDocument()),
      () -> assertEquals(statusId, dto.statusId()),
      () -> assertEquals(loanTypeId, dto.loanTypeId())
    );
  }
}