package co.com.pragma.api;

import co.com.pragma.api.config.ApplicationPath;
import co.com.pragma.api.constants.ApplicationWebKeys;
import co.com.pragma.api.dto.ApplicationResponseDto;
import co.com.pragma.api.dto.CreateApplicationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

  private final ApplicationPath applicationPath;

  public RouterRest(ApplicationPath applicationPath) {
    this.applicationPath = applicationPath;
  }

  @RouterOperations({
    @RouterOperation(
      path = ApplicationWebKeys.OPEN_API_APPLICATION_PATH,
      method = RequestMethod.POST,
      beanClass = Handler.class,
      beanMethod = ApplicationWebKeys.OPEN_API_BEAN_METHOD_SAVE_APPLICATION,
      operation = @Operation(
        operationId = ApplicationWebKeys.OPEN_API_OPERATION_ID, responses = {
        @ApiResponse(
          responseCode = ApplicationWebKeys.OPEN_API_RESPONSE_CODE,
          description = ApplicationWebKeys.OPEN_API_DESCRIPTION_SUCCESS,
          content = @Content(mediaType = ApplicationWebKeys.OPEN_API_MEDIA_TYPE,
            schema = @Schema(implementation = ApplicationResponseDto.class))
        )
      },
        requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = CreateApplicationDto.class)))
      ))
  })
  @Bean
  public RouterFunction<ServerResponse> routerFunction(Handler handler) {
    return route(POST(applicationPath.getApplication()), handler::listenSaveApplication);
  }

}
