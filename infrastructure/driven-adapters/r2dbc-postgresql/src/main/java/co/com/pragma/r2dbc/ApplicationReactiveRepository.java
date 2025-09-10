package co.com.pragma.r2dbc;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
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
         "s.name AS application_status, " +
         "s.status_id " +
         "FROM applications a " +
         "LEFT JOIN type_of_loan lt ON a.loan_type_id = lt.loan_type_id " +
         "LEFT JOIN status s ON a.status_id = s.status_id " +
         "WHERE (:status IS NULL OR s.status_id = :status) AND (:loanType IS NULL OR lt.loan_type_id = :loanType) " +
         "AND (:email IS NULL OR a.email = :email) " +
         "LIMIT :pageSize OFFSET (:pageNumber - 1) * :pageSize")
  Flux<ApplicationDataEntity> findByStatusAndLoanTypeAndEmail(@Param("email") String email, @Param("status") Integer status, @Param("loanType") Integer loanType, @Param("pageSize") Integer pageSize, @Param("pageNumber") Integer pageNumber);

  @Query("SELECT COUNT(*) FROM applications a " +
         "LEFT JOIN type_of_loan lt ON a.loan_type_id = lt.loan_type_id " +
         "LEFT JOIN status s ON a.status_id = s.status_id " +
         "WHERE (:status IS NULL OR s.status_id = :status) AND (:loanType IS NULL OR lt.loan_type_id = :loanType) " +
         "AND (:email IS NULL OR a.email = :email) ")
  Mono<Long> countByStatusAndLoanTypeAndEmail(@Param("email") String email, @Param("status") Integer status, @Param("loanType") Integer loanType);

}
