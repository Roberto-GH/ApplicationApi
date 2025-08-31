package co.com.pragma.model.application;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ApplicationData {

  private UUID id;
  private BigDecimal amount;
  private String term;
  private String email;
  private String loanType;
  private BigDecimal interestRate;
  private String applicationStatus;

}


