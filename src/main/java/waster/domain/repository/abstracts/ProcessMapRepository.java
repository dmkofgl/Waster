package waster.domain.repository.abstracts;

import org.springframework.data.repository.CrudRepository;
import waster.domain.entity.ProcessMap;

import java.util.Optional;

public interface ProcessMapRepository extends CrudRepository<ProcessMap, Long> {
    Optional<ProcessMap> getByArticleId(Long id);
}
