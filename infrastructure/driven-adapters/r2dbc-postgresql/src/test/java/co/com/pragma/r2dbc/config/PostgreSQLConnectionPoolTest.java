package co.com.pragma.r2dbc.config;

import io.r2dbc.spi.ConnectionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostgreSQLConnectionPoolTest {

  @Mock
  private PostgresqlConnectionProperties properties;

  @InjectMocks
  private PostgreSQLConnectionPool connectionPool;

  @Test
  void getConnectionConfigSuccess() {
    when(properties.host()).thenReturn("localhost");
    when(properties.port()).thenReturn(5432);
    when(properties.database()).thenReturn("dbName");
    when(properties.schema()).thenReturn("schema");
    when(properties.username()).thenReturn("username");
    when(properties.password()).thenReturn("password");
    assertNotNull(connectionPool.getConnectionConfig(properties));
  }

  @Test
  void transactionManager() {
    ConnectionFactory connectionFactory = mock(ConnectionFactory.class);
    assertNotNull(connectionPool.transactionManager(connectionFactory));
  }

}
