package co.com.pragma.r2dbc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("type_of_loan")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class LoanTypeEntity {

  @Id
  @Column(PostgreSQLKeys.COLUMN_NAME_LOAN_TYPE_ID)
  private Long loanTypeId;
  private String name;
  @Column(PostgreSQLKeys.COLUMN_NAME_MINIMUN_AMOUNT)
  private BigDecimal minimumAmount;
  @Column(PostgreSQLKeys.COLUMN_NAME_MAXIMUN_AMOUNT)
  private BigDecimal maximumAmount;
  @Column(PostgreSQLKeys.COLUMN_NAME_INTEREST_RATE)
  private BigDecimal interestRate;
  @Column(PostgreSQLKeys.COLUMN_NAME_AUTOMATIC_VALIDATION)
  private Boolean automaticValidation;

}
