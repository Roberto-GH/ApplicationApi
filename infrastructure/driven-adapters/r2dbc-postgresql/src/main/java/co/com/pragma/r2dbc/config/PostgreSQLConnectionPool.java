package co.com.pragma.r2dbc.config;

import co.com.pragma.r2dbc.PostgreSQLKeys;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;

import java.time.Duration;

@Configuration
public class PostgreSQLConnectionPool {

  @Bean
  public ConnectionPool getConnectionConfig(PostgresqlConnectionProperties properties) {
    PostgresqlConnectionConfiguration dbConfiguration = PostgresqlConnectionConfiguration
      .builder()
      .host(properties.host())
      .port(properties.port())
      .database(properties.database())
      .schema(properties.schema())
      .username(properties.username())
      .password(properties.password())
      .build();
    ConnectionPoolConfiguration poolConfiguration = ConnectionPoolConfiguration
      .builder()
      .connectionFactory(new PostgresqlConnectionFactory(dbConfiguration))
      .name(PostgreSQLKeys.API_POSTGRESS_CONNECTION_POOL)
      .initialSize(PostgreSQLKeys.INITIAL_SIZE)
      .maxSize(PostgreSQLKeys.MAX_SIZE)
      .maxIdleTime(Duration.ofMinutes(PostgreSQLKeys.MAX_IDLE_TIME))
      .validationQuery(PostgreSQLKeys.VALIDATION_QUERY)
      .build();
    return new ConnectionPool(poolConfiguration);
  }

  @Bean
  public ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
    return new R2dbcTransactionManager(connectionFactory);
  }

}