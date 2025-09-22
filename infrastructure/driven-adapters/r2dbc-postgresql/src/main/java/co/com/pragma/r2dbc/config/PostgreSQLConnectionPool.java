package co.com.pragma.r2dbc.config;

import co.com.bancolombia.secretsmanager.api.GenericManagerAsync;
import co.com.bancolombia.secretsmanager.api.exceptions.SecretException;
import co.com.pragma.r2dbc.constants.PostgreSQLKeys;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Configuration
public class PostgreSQLConnectionPool {

  private static final Logger LOG = LoggerFactory.getLogger(PostgreSQLConnectionPool.class);
  private final GenericManagerAsync genericManager;
  private final String dbSecretName;

  public PostgreSQLConnectionPool(GenericManagerAsync genericManager, @Value("${aws.dbSecretName}") String dbSecretName) {
    this.genericManager = genericManager;
    this.dbSecretName = dbSecretName;
  }

  @Bean
  public ConnectionPool getConnectionConfig(PostgresqlConnectionProperties properties) {
    PostgresqlConnectionConfiguration dbConfiguration = PostgresqlConnectionConfiguration
      .builder()
      .host(properties.host())
      .port(properties.port())
      .database(properties.database())
      .schema(properties.schema())
      .username(getDbSecretModelReactive().map(DbSecretModel::getUsername))
      .password(getDbSecretModelReactive().map(DbSecretModel::getPassword))
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

  private Mono<DbSecretModel> getDbSecretModelReactive() {
    try {
      return genericManager
        .getSecret(dbSecretName, DbSecretModel.class)
        .onErrorResume(SecretException.class, e -> Mono.error(new RuntimeException("Error retrieving JWT secret", e)));
    } catch (SecretException e) {
      LOG.error("Synchronous error retrieving secret: {}", e.getMessage());
      return Mono.error(new RuntimeException("Synchronous error retrieving JWT secret", e));
    }
  }



  @Bean
  public ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
    return new R2dbcTransactionManager(connectionFactory);
  }

}