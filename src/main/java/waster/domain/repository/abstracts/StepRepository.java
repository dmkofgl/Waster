package waster.domain.repository.abstracts;

import org.springframework.data.repository.CrudRepository;
import waster.domain.entity.Step;

public interface StepRepository  extends CrudRepository<Step,Long> {
}
