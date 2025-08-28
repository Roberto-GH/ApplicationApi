package co.com.pragma.model.application;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class StatusTest {

  private static Status status;

  @BeforeAll
  static void setUpAll() {
    status = Status.builder().statusId(1L).name("name").description("description").build();
  }

  @Test
  void getStatusId() {
    Assertions.assertEquals(1L, status.getStatusId());
  }

  @Test
  void getName() {
    Assertions.assertEquals("name", status.getName());
  }

  @Test
  void getDescription() {
    Assertions.assertEquals("description", status.getDescription());
  }

}