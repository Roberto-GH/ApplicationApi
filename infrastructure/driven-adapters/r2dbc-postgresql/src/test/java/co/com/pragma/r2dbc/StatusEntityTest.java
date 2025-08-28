package co.com.pragma.r2dbc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusEntityTest {

  @Test
  void testStatusEntity() {
    Long statusId = 1L;
    String name = "Pending";
    String description = "Application is pending review";
    StatusEntity statusEntity = new StatusEntity(statusId, name, description);
    assertAll(() -> assertEquals(statusId, statusEntity.getStatus_id()), () -> assertEquals(name, statusEntity.getName()),
              () -> assertEquals(description, statusEntity.getDescription()));
    // Test setters
    Long newStatusId = 2L;
    String newName = "Approved";
    String newDescription = "Application has been approved";
    statusEntity.setStatus_id(newStatusId);
    statusEntity.setName(newName);
    statusEntity.setDescription(newDescription);
    assertAll(() -> assertEquals(newStatusId, statusEntity.getStatus_id()), () -> assertEquals(newName, statusEntity.getName()),
              () -> assertEquals(newDescription, statusEntity.getDescription()));
  }

  @Test
  void testNoArgsConstructor() {
    StatusEntity statusEntity = new StatusEntity();
    assertNotNull(statusEntity);
  }

}
