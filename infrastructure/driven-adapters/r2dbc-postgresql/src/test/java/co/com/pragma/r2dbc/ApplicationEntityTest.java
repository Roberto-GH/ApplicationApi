package co.com.pragma.r2dbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationEntityTest {

  private static final UUID applicationId = UUID.randomUUID();
  private static final BigDecimal amount = new BigDecimal("1000.00");
  private static final String term = "12 months";
  private static final String email = "test@example.com";
  private static final Long identityDocument = 123456789L;
  private static final Long statusId = 1L;
  private static final Long loanTypeId = 2L;

  private ApplicationEntity applicationEntity;

  @BeforeEach
  void setUp() {
    applicationEntity = new ApplicationEntity(applicationId, amount, term, email, identityDocument, statusId, loanTypeId);
  }

  @Test
  void testApplicationEntity() {
    assertAll(
      () -> assertEquals(applicationId, applicationEntity.getApplicationId()), () -> assertEquals(amount, applicationEntity.getAmount()),
      () -> assertEquals(term, applicationEntity.getTerm()), () -> assertEquals(email, applicationEntity.getEmail()),
      () -> assertEquals(identityDocument, applicationEntity.getIdentityDocument()), () -> assertEquals(statusId, applicationEntity.getStatusId()),
      () -> assertEquals(loanTypeId, applicationEntity.getLoanTypeId())
    );
  }

  @Test
  void testSetters() {
    UUID newApplicationId = UUID.randomUUID();
    BigDecimal newAmount = new BigDecimal("2000.00");
    String newTerm = "24 months";
    String newEmail = "newtest@example.com";
    Long newIdentityDocument = 987654321L;
    Long newStatusId = 3L;
    Long newLoanTypeId = 4L;
    applicationEntity.setApplicationId(newApplicationId);
    applicationEntity.setAmount(newAmount);
    applicationEntity.setTerm(newTerm);
    applicationEntity.setEmail(newEmail);
    applicationEntity.setIdentityDocument(newIdentityDocument);
    applicationEntity.setStatusId(newStatusId);
    applicationEntity.setLoanTypeId(newLoanTypeId);
    assertAll(
      () -> assertEquals(newApplicationId, applicationEntity.getApplicationId()),
      () -> assertEquals(newAmount, applicationEntity.getAmount()),
      () -> assertEquals(newTerm, applicationEntity.getTerm()),
      () -> assertEquals(newEmail, applicationEntity.getEmail()),
      () -> assertEquals(newIdentityDocument, applicationEntity.getIdentityDocument()),
      () -> assertEquals(newStatusId, applicationEntity.getStatusId()),
      () -> assertEquals(newLoanTypeId, applicationEntity.getLoanTypeId())
    );
  }

  @Test
  void testNoArgsConstructor() {
    ApplicationEntity applicationEntity = new ApplicationEntity();
    assertNotNull(applicationEntity);
  }

}
