package co.com.pragma.model.application;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

class ApplicationTest {

  private static Application application;

  @BeforeAll
  static void setUpAll() {
    application = Application
      .builder()
      .applicationId(UUID.fromString("a3a4e4a0-1b2c-4d5e-8f6a-7b8c9d0e1f2a"))
      .amount(BigDecimal.TEN)
      .term(12)
      .email("email")
      .identityDocument(123456L)
      .build();
  }

  @Test
  void build() {
    Application build = Application.builder().statusId(1L).loanTypeId(1L).build();
    Assertions.assertEquals(UUID.fromString("a3a4e4a0-1b2c-4d5e-8f6a-7b8c9d0e1f2a"), application.getApplicationId());
    Assertions.assertAll(
      () -> Assertions.assertEquals(1L, build.getStatusId()),
      () -> Assertions.assertEquals(1L, build.getLoanTypeId())
    );
  }

  @Test
  void buildEquals0() {
    Application build = Application.builder().statusId(0L).loanTypeId(0L).build();
    Assertions.assertEquals(UUID.fromString("a3a4e4a0-1b2c-4d5e-8f6a-7b8c9d0e1f2a"), application.getApplicationId());
    Assertions.assertAll(
      () -> Assertions.assertEquals(1L, build.getStatusId()),
      () -> Assertions.assertEquals(1L, build.getLoanTypeId())
    );
  }

  @Test
  void getApplicationId() {
    Assertions.assertEquals(UUID.fromString("a3a4e4a0-1b2c-4d5e-8f6a-7b8c9d0e1f2a"), application.getApplicationId());
  }

  @Test
  void getAmount() {
    Assertions.assertEquals(BigDecimal.TEN, application.getAmount());
  }

  @Test
  void getTerm() {
    Assertions.assertEquals(12, application.getTerm());
  }

  @Test
  void getEmail() {
    Assertions.assertEquals("email", application.getEmail());
  }

  @Test
  void getIdentityDocument() {
    Assertions.assertEquals(123456L, application.getIdentityDocument());
  }

  @Test
  void getStatusId() {
    Assertions.assertEquals(1L, application.getStatusId());
  }

  @Test
  void getLoanTypeId() {
    Assertions.assertEquals(1L, application.getLoanTypeId());
  }

}
