package co.com.pragma.model.application.enums;

public enum QueueAlias {

  VALIDATIONS("validation-queue"),
  NOTIFICATIONS("notification-queue");

  private final String alias;

  QueueAlias(String alias) {
    this.alias = alias;
  }

  public String getAlias() {
    return alias;
  }

}
