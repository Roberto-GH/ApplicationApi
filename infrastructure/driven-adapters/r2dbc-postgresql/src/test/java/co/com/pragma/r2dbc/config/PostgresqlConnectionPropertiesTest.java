package co.com.pragma.r2dbc.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PostgresqlConnectionPropertiesTest {

  private PostgresqlConnectionProperties properties;

  @BeforeEach
  void setUp() {
    properties = new PostgresqlConnectionProperties("localhost", 5432, "testdb", "public", "user", "password");
  }

  @Test
  void host() {
    assertEquals("localhost", properties.host());
  }

  @Test
  void port() {
    assertEquals(5432, properties.port());
  }

  @Test
  void database() {
    assertEquals("testdb", properties.database());
  }

  @Test
  void schema() {
    assertEquals("public", properties.schema());
  }

  @Test
  void username() {
    assertEquals("user", properties.username());
  }

  @Test
  void password() {
    assertEquals("password", properties.password());
  }

}
