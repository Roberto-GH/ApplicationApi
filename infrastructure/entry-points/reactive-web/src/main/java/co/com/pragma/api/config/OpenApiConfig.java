package co.com.pragma.api.config;

import co.com.pragma.api.constants.ApplicationWebKeys;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
  info = @Info(
    title = ApplicationWebKeys.OPEN_API_TITLE,
    version = ApplicationWebKeys.OPEN_API_VERSION,
    description = ApplicationWebKeys.OPEN_API_DESCRIPTION
  ),
  security = @SecurityRequirement(name = ApplicationWebKeys.OPEN_API_SECURITY_NAME)
)
@SecurityScheme(
  name = ApplicationWebKeys.OPEN_API_SECURITY_NAME,
  type = SecuritySchemeType.HTTP,
  scheme = ApplicationWebKeys.OPEN_API_SCHEME,
  bearerFormat = ApplicationWebKeys.OPEN_API_BEARER_FORMAT
)
public class OpenApiConfig {
}
