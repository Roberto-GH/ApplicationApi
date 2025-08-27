package co.com.pragma.model.application;

public class Status {

  private final Long status_id;
  private final String name;
  private final String description;

  private Status(Builder builder) {
    this.status_id = builder.status_id;
    this.name = builder.name;
    this.description = builder.description;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Long getStatus_id() {
    return status_id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public static class Builder {

    private Long status_id;
    private String name;
    private String description;

    public Builder status_id(Long status_id) {
      this.status_id = status_id;
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

