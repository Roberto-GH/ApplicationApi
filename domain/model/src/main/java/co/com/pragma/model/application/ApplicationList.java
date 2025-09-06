package co.com.pragma.model.application;

import java.util.List;

public record ApplicationList(Integer pageNumber, Integer pageSize, Integer totalRecords, Integer totalPages, List<ApplicationData> data) {

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalRecords;
    private Integer totalPages;
    private List<ApplicationData> data;

    public Builder pageNumber(Integer pageNumber) {
      this.pageNumber = pageNumber;
      return this;
    }

    public Builder pageSize(Integer pageSize) {
      this.pageSize = pageSize;
      return this;
    }

    public Builder totalRecords(Integer totalRecords) {
      this.totalRecords = totalRecords;
      return this;
    }

    public Builder totalPages(Integer totalPages) {
      this.totalPages = totalPages;
      return this;
    }

    public Builder data(List<ApplicationData> data) {
      this.data = data;
      return this;
    }

    public ApplicationList build() {
      return new ApplicationList(pageNumber, pageSize, totalRecords, totalPages, data);
    }

  }

}
