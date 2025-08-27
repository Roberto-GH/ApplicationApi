package co.com.pragma.r2dbc;

import co.com.pragma.model.application.LoanType;
import co.com.pragma.model.application.gateways.LoanTypeRepository;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class LoanTypeReactiveRepositoryAdapter
  extends ReactiveAdapterOperations<LoanType, LoanTypeEntity, Long, LoanTypeReactiveRepository>
  implements LoanTypeRepository {

  public LoanTypeReactiveRepositoryAdapter(LoanTypeReactiveRepository repository, ObjectMapper mapper) {
    super(repository, mapper, entity -> mapper.mapBuilder(entity, LoanType.Builder.class).build());
  }


}
