package co.com.pragma.r2dbc;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ApplicationReactiveRepository extends ReactiveCrudRepository<ApplicationEntity, UUID>, ReactiveQueryByExampleExecutor<ApplicationEntity> {

  @Query("SELECT " +
         "a.application_id AS id, " +
         "a.amount, " +
         "a.term, " +
         "a.email, " +
         "lt.name AS loan_type, " +
         "lt.interest_rate, " +
         "s.name AS application_status " +
         "FROM applications a " +
         "LEFT JOIN type_of_loan lt ON a.loan_type_id = lt.loan_type_id " +
         "LEFT JOIN status s ON a.status_id = s.status_id " +
         "WHERE (s.status_id = COALESCE(:status, s.status_id)) AND (lt.loan_type_id = COALESCE(:loanType, lt.loan_type_id)) " +
         "LIMIT :pageSize OFFSET (:pageNumber - 1) * :pageSize")
  Flux<ApplicationDataEntity> findByStatusAndLoanType(Integer status, Integer loanType, Integer pageSize, Integer pageNumber);

  @Query("SELECT COUNT(*) FROM applications a " +
         "LEFT JOIN type_of_loan lt ON a.loan_type_id = lt.loan_type_id " +
         "LEFT JOIN status s ON a.status_id = s.status_id " +
         "WHERE (s.status_id = COALESCE(:status, s.status_id)) AND (lt.loan_type_id = COALESCE(:loanType, lt.loan_type_id))")
  Mono<Long> countByStatusAndLoanType(Integer status, Integer loanType);

}
