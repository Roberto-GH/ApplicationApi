package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.ApplicationResponseDto;
import co.com.pragma.api.dto.CreateApplicationDto;
import co.com.pragma.model.application.Application;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ApplicationDtoMapper {

  ApplicationResponseDto toResponseDto(Application user);

  Application.Builder toBuilder(CreateApplicationDto dto);

  default Application.Builder toModel(CreateApplicationDto dto) {
    if (dto == null)
      return null;
    return toBuilder(dto);
  }

}
