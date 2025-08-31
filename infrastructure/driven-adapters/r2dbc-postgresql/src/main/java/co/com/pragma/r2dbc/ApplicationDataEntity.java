package co.com.pragma.r2dbc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ApplicationDataEntity {

  @Id
  private UUID id;
  private BigDecimal amount;
  private String term;
  private String email;
  @Column("loan_type")
  private String loanType;
  @Column("interest_rate")
  private BigDecimal interestRate;
  @Column("application_status")
  private String applicationStatus;

}
