package waster.domain.repository.abstracts;

import org.springframework.data.repository.CrudRepository;
import waster.domain.entity.Bench;

public interface BenchRepository extends CrudRepository<Bench,Long> {
}
