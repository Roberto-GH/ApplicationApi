package co.com.pragma.r2dbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusEntityTest {

  private final Long statusId = 1L;
  private final String name = "Pending";
  private final String description = "Application is pending review";
  private StatusEntity statusEntity;

  @BeforeEach
  void setUp() {
    statusEntity = new StatusEntity(statusId, name, description);
  }

  @Test
  void testStatusEntity() {
    assertAll(
        () -> assertEquals(statusId, statusEntity.getStatusId()),
        () -> assertEquals(name, statusEntity.getName()),
        () -> assertEquals(description, statusEntity.getDescription()
      ));
  }

  @Test
  void testSetters() {
    Long newStatusId = 2L;
    String newName = "Approved";
    String newDescription = "Application has been approved";
    statusEntity.setStatusId(newStatusId);
    statusEntity.setName(newName);
    statusEntity.setDescription(newDescription);
    assertAll(
        () -> assertEquals(newStatusId, statusEntity.getStatusId()),
        () -> assertEquals(newName, statusEntity.getName()),
        () -> assertEquals(newDescription, statusEntity.getDescription()
      ));
  }

  @Test
  void testNoArgsConstructor() {
    StatusEntity statusEntity = new StatusEntity();
    assertNotNull(statusEntity);
  }

}
