package co.com.pragma.consumer;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class LoginRequest {

  private String email;
  private String password;

}
