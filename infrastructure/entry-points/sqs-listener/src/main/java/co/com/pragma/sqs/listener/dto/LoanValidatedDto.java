package co.com.pragma.sqs.listener.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class LoanValidatedDto {

  private String applicantEmail;
  private String loanId;
  private String status;
  private String reason;
  private Long statusId;

}
