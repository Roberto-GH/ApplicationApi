package co.com.pragma.api.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ApplicationListResponseDto(
  Integer pageNumber,
  Integer pageSize,
  Integer totalRecords,
  Integer totalPages,
  List<ApplicationDataDto> data
) {}
