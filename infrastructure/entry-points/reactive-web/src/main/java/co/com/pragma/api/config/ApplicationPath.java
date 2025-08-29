package co.com.pragma.api.config;

import co.com.pragma.api.ApplicationWebKeys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = ApplicationWebKeys.PATH_ROUTE_APPLICATION)
public class ApplicationPath {

  private String application;

}
