package co.com.pragma.r2dbc.helper;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.ParameterizedType;
import java.util.function.Function;

public abstract class ReactiveAdapterOperations<D, E, ID, R extends ReactiveCrudRepository<E, ID> & ReactiveQueryByExampleExecutor<E>> {

  protected R repository;
  protected ObjectMapper mapper;
  private final Class<E> dataClass;
  private final Function<E, D> toDomainFn;

  @SuppressWarnings("unchecked")
  protected ReactiveAdapterOperations(R repository, ObjectMapper mapper, Function<E, D> toDomainFn) {
    this.repository = repository;
    this.mapper = mapper;
    ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
    this.dataClass = (Class<E>) genericSuperclass.getActualTypeArguments()[1];
    this.toDomainFn = toDomainFn;
  }

  protected E toEntity(D domain) {
    return mapper.map(domain, dataClass);
  }

  protected D toDomain(E entity) {
    return entity != null ? toDomainFn.apply(entity) : null;
  }

  public Mono<D> save(D domain) {
    return saveData(toEntity(domain)).map(this::toDomain);
  }

  protected Flux<D> saveAllEntities(Flux<D> domains) {
    return saveData(domains.map(this::toEntity)).map(this::toDomain);
  }

  protected Mono<E> saveData(E entity) {
    return repository.save(entity);
  }

  protected Flux<E> saveData(Flux<E> entity) {
    return repository.saveAll(entity);
  }

  public Mono<D> findById(ID id) {
    return repository.findById(id).map(this::toDomain);
  }

  public Flux<D> findByExample(D domain) {
    return repository.findAll(Example.of(toEntity(domain))).map(this::toDomain);
  }

  public Flux<D> findAll() {
    return repository.findAll().map(this::toDomain);
  }

}
