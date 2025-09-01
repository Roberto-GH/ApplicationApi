package co.com.pragma.api.mapper;

import co.com.pragma.api.constants.ApplicationWebKeys;
import co.com.pragma.api.dto.ApplicationResponseDto;
import co.com.pragma.api.dto.CreateApplicationDto;
import co.com.pragma.api.dto.ApplicationListResponseDto;
import co.com.pragma.model.application.Application;
import co.com.pragma.model.application.ApplicationList;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = ApplicationWebKeys.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ApplicationDtoMapper {

  ApplicationResponseDto toResponseDto(Application user);

  Application.Builder toBuilder(CreateApplicationDto dto);

  default Application.Builder toModel(CreateApplicationDto dto) {
    if (dto == null)
      return null;
    return toBuilder(dto);
  }

  ApplicationListResponseDto toApplicationListDto(ApplicationList applicationList);

}
