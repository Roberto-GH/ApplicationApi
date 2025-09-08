package co.com.pragma.model.application;

public class PathApplication {

  private String applicationId;
  private Long statusId;

  public String getApplicationId() {
    return applicationId;
  }

  public void setApplicationId(String applicationId) {
    this.applicationId = applicationId;
  }

  public Long getStatusId() {
    return statusId;
  }

  public void setStatusId(Long statusId) {
    this.statusId = statusId;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private String applicationId;
    private Long statusId;

    public Builder applicationId(String applicationId) {
      this.applicationId = applicationId;
      return this;
    }

    public Builder statusId(Long statusId) {
      this.statusId = statusId;
      return this;
    }

    public PathApplication build() {
      PathApplication dto = new PathApplication();
      dto.setApplicationId(applicationId);
      dto.setStatusId(statusId);
      return dto;
    }

  }

}
