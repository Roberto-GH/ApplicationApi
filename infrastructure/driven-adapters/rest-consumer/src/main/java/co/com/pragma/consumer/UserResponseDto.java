package co.com.pragma.consumer;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class UserResponseDto {

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

}
