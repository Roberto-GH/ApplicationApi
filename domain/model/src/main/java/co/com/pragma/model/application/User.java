package co.com.pragma.model.application;

import java.time.LocalDate;
import java.util.UUID;

public class User {

  private final UUID userId;
  private final String firstName;
  private final String middleName;
  private final String lastName;
  private final String secondLastName;
  private final String email;
  private final String password;
  private final Long identityDocument;
  private final LocalDate birthdate;
  private final String address;
  private final Long numberPhone;
  private final Long baseSalary;
  private final Long rolId;

  private User(Builder builder) {
    this.userId = builder.userId;
    this.firstName = builder.firstName;
    this.middleName = builder.middleName;
    this.lastName = builder.lastName;
    this.secondLastName = builder.secondLastName;
    this.email = builder.email;
    this.password = builder.password;
    this.identityDocument = builder.identityDocument;
    this.birthdate = builder.birthdate;
    this.address = builder.address;
    this.numberPhone = builder.numberPhone;
    this.baseSalary = builder.baseSalary;
    this.rolId = builder.rolId;
  }

  public UUID getUserId() {
    return userId;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getMiddleName() {
    return middleName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getSecondLastName() {
    return secondLastName;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public Long getIdentityDocument() {
    return identityDocument;
  }

  public LocalDate getBirthdate() {
    return birthdate;
  }

  public String getAddress() {
    return address;
  }

  public Long getNumberPhone() {
    return numberPhone;
  }

  public Long getBaseSalary() {
    return baseSalary;
  }

  public Long getRolId() {
    return rolId;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private UUID userId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String secondLastName;
    private String email;
    private String password;
    private Long identityDocument;
    private LocalDate birthdate;
    private String address;
    private Long numberPhone;
    private Long baseSalary;
    private Long rolId;

    public Builder userId(UUID userId) {
      this.userId = userId;
      return this;
    }

    public Builder firstName(String firstName) {
      this.firstName = firstName;
      return this;
    }

    public Builder middleName(String middleName) {
      this.middleName = middleName;
      return this;
    }

    public Builder lastName(String lastName) {
      this.lastName = lastName;
      return this;
    }

    public Builder secondLastName(String secondLastName) {
      this.secondLastName = secondLastName;
      return this;
    }

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    public Builder password(String password) {
      this.password = password;
      return this;
    }

    public Builder identityDocument(Long identityDocument) {
      this.identityDocument = identityDocument;
      return this;
    }

    public Builder birthdate(LocalDate birthdate) {
      this.birthdate = birthdate;
      return this;
    }

    public Builder address(String address) {
      this.address = address;
      return this;
    }

    public Builder numberPhone(Long numberPhone) {
      this.numberPhone = numberPhone;
      return this;
    }

    public Builder baseSalary(Long baseSalary) {
      this.baseSalary = baseSalary;
      return this;
    }

    public Builder rolId(Long rolId) {
      this.rolId = rolId;
      return this;
    }

    public User build() {
      return new User(this);
    }

  }

}

