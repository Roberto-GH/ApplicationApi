package co.com.pragma.r2dbc;

import co.com.pragma.r2dbc.constants.PostgreSQLKeys;
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
  @Column(PostgreSQLKeys.COLUMN_NAME_LOAN_TYPE)
  private String loanType;
  @Column(PostgreSQLKeys.COLUMN_NAME_INTEREST_RATE)
  private BigDecimal interestRate;
  @Column(PostgreSQLKeys.COLUMN_NAME_APPLICATION_STATUS)
  private String applicationStatus;
  @Column(PostgreSQLKeys.COLUMN_NAME_STATUS_ID)
  private Long statusId;

}
