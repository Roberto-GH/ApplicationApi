package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.ApplicationResponseDto;
import co.com.pragma.api.dto.CreateApplicationDto;
import co.com.pragma.model.application.Application;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationDtoMapperTest {

  private ApplicationDtoMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = Mappers.getMapper(ApplicationDtoMapper.class);
  }

  @Test
  void toResponseDto_shouldMapApplicationToApplicationResponseDto() {
    UUID applicationId = UUID.randomUUID();
    Application application = Application
      .builder()
      .applicationId(applicationId)
      .amount(new BigDecimal("1000.00"))
      .term("12 months")
      .email("test@example.com")
      .identityDocument(123456789L)
      .statusId(1L)
      .loanTypeId(2L)
      .build();
    ApplicationResponseDto dto = mapper.toResponseDto(application);
    assertNotNull(dto);
    assertAll(
      () -> assertEquals(application.getApplicationId(), dto.applicationId()), () -> assertEquals(application.getAmount(), dto.amount()),
      () -> assertEquals(application.getTerm(), dto.term()), () -> assertEquals(application.getEmail(), dto.email()),
      () -> assertEquals(application.getIdentityDocument(), dto.identityDocument()), () -> assertEquals(application.getStatusId(), dto.statusId()),
      () -> assertEquals(application.getLoanTypeId(), dto.loanTypeId())
    );
  }

  @Test
  void toBuilder_shouldMapCreateApplicationDtoToApplicationBuilder() {
    CreateApplicationDto createDto = new CreateApplicationDto(new BigDecimal("1500.00"), "24 months", "create@example.com", 987654321L, 3L);
    Application.Builder builder = mapper.toBuilder(createDto);
    assertNotNull(builder);
    Application builtApplication = builder.build();
    assertAll(
      () -> assertEquals(createDto.amount(), builtApplication.getAmount()), () -> assertEquals(createDto.term(), builtApplication.getTerm()),
      () -> assertEquals(createDto.email(), builtApplication.getEmail()), () -> assertEquals(createDto.identityDocument(), builtApplication.getIdentityDocument()),
      () -> assertEquals(createDto.loanTypeId(), builtApplication.getLoanTypeId())
    );
  }

  @Test
  void toModel_shouldMapCreateApplicationDtoToApplicationBuilder() {
    CreateApplicationDto createDto = new CreateApplicationDto(new BigDecimal("2000.00"), "36 months", "model@example.com", 1122334455L, 4L);
    Application.Builder builder = mapper.toModel(createDto);
    assertNotNull(builder);
    Application builtApplication = builder.build();
    assertAll(
      () -> assertEquals(createDto.amount(), builtApplication.getAmount()), () -> assertEquals(createDto.term(), builtApplication.getTerm()),
      () -> assertEquals(createDto.email(), builtApplication.getEmail()), () -> assertEquals(createDto.identityDocument(), builtApplication.getIdentityDocument()),
      () -> assertEquals(createDto.loanTypeId(), builtApplication.getLoanTypeId())
    );
  }

  @Test
  void toModel_shouldReturnNullWhenDtoIsNull() {
    Application.Builder builder = mapper.toModel(null);
    assertNull(builder);
  }

  @Test
  void toModel_shouldReturnNullWhenResponsIsNull() {
    ApplicationResponseDto builder = mapper.toResponseDto(null);
    assertNull(builder);
  }

  @Test
  void toModel_shouldReturnNullWhenBuilderIsNull() {
    Application.Builder builder = mapper.toBuilder(null);
    assertNull(builder);
  }

}
