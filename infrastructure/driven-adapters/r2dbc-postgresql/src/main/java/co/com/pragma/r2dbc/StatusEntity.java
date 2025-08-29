package co.com.pragma.r2dbc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("status")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class StatusEntity {

  @Id
  @Column("status_id")
  private Long statusId;
  private String name;
  private String description;

}
