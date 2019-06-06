package waster.domain.repository.abstracts;

import org.springframework.data.repository.CrudRepository;
import waster.domain.entity.Bench;

import java.util.List;

public interface BenchRepository extends CrudRepository<Bench, Long> {
    List<Bench> findByMachineNumberNew(Long machineId);
}
