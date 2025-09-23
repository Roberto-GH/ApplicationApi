package co.com.pragma.consumer.config;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthSecretModel {

  @SerializedName("ADMIN_EMAIL")
  private String email;
  @SerializedName("ADMIN_PASSWORD")
  private String password;

}
