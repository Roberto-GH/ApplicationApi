package co.com.pragma.r2dbc.config;

import co.com.pragma.r2dbc.constants.PostgreSQLKeys;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = PostgreSQLKeys.PATH_CONFIG_R2DBC)
public record PostgresqlConnectionProperties(
  String host,
  Integer port,
  String database,
  String schema,
  String username,
  String password
) {}
