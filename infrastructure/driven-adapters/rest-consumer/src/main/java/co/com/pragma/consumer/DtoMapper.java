package co.com.pragma.consumer;

import co.com.pragma.model.application.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DtoMapper {

  User toUser(UserResponseDto userResponseDto);

}
