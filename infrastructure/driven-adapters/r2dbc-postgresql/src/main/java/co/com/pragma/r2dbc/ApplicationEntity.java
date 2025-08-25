package co.com.pragma.r2dbc;

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
  @Column("application_id")
  private UUID applicationId;
  private BigDecimal amount;
  private String term;
  private String email;
  @Column("identity_document")
  private Long identityDocument;
  @Column("status_id")
  private Long statusId;
  @Column("loan_type_id")
  private Long loanTypeId;

}
