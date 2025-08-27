package co.com.pragma.model.application;

public class Status {

  private final Long statusId;
  private final String name;
  private final String description;

  private Status(Builder builder) {
    this.statusId = builder.statusId;
    this.name = builder.name;
    this.description = builder.description;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Long getStatusId() {
    return statusId;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public static class Builder {

    private Long statusId;
    private String name;
    private String description;

    public Builder statusId(Long statusId) {
      this.statusId = statusId;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Status build() {
      return new Status(this);
    }

  }

}

