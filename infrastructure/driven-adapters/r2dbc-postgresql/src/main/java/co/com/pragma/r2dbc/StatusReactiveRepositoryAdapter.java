package co.com.pragma.r2dbc;

import co.com.pragma.model.application.Status;
import co.com.pragma.model.application.gateways.StatusRepository;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class StatusReactiveRepositoryAdapter
  extends ReactiveAdapterOperations<Status, StatusEntity, Long, StatusReactiveRepository>
  implements StatusRepository {

  public StatusReactiveRepositoryAdapter(StatusReactiveRepository repository, ObjectMapper mapper) {
    super(repository, mapper, entity -> mapper.mapBuilder(entity, Status.Builder.class).build());
  }

}

