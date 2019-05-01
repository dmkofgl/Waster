package waster.domain.repository.abstracts;

import org.springframework.data.repository.CrudRepository;
import waster.domain.entity.Machine;

public interface MachineRepository extends CrudRepository<Machine, Long> {
}
