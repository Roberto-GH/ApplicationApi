package co.com.pragma.r2dbc;

import co.com.pragma.r2dbc.constants.PostgreSQLKeys;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("status")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class StatusEntity {

  @Id
  @Column(PostgreSQLKeys.COLUMN_NAME_STATUS_ID)
  private Long statusId;
  private String name;
  private String description;

}
