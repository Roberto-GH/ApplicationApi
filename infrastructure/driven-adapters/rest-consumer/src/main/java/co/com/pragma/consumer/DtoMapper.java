package co.com.pragma.consumer;

import co.com.pragma.consumer.constants.RestConsumerKeys;
import co.com.pragma.model.application.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = RestConsumerKeys.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DtoMapper {

  User toUser(UserResponseDto userResponseDto);

}
