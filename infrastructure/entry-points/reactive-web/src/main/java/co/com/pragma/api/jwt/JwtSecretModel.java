package co.com.pragma.api.jwt;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtSecretModel {

  @SerializedName("jwt_secret_key")
  private String jwtSecretKey;

  @SerializedName("jwt_expiration_value")
  private Integer jwtExpirationValue;

}