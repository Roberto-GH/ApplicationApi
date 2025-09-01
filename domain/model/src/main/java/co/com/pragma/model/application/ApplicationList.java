package co.com.pragma.model.application;

import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public record ApplicationList(
  Integer pageNumber,
  Integer pageSize,
  Integer totalRecords,
  Integer totalPages,
  List<ApplicationData> data
) {
}
