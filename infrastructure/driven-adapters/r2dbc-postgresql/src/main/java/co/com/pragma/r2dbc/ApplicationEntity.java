package co.com.pragma.r2dbc;

import co.com.pragma.r2dbc.constants.PostgreSQLKeys;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Table("applications")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ApplicationEntity {

  @Id
  @Column(PostgreSQLKeys.COLUMN_NAME_APPLICATION_ID)
  private UUID applicationId;
  private BigDecimal amount;
  private Integer term;
  private String email;
  @Column(PostgreSQLKeys.COLUMN_NAME_IDENTITY)
  private Long identityDocument;
  @Column(PostgreSQLKeys.COLUMN_NAME_STATUS_ID)
  private Long statusId;
  @Column(PostgreSQLKeys.COLUMN_NAME_LOAN_TYPE_ID)
  private Long loanTypeId;

}
