package waster.domain.repository.abstracts;

import org.springframework.data.jpa.repository.JpaRepository;
import waster.domain.entity.calendar.Interruption;

public interface InterruptionRepository extends JpaRepository<Interruption, Long> {
}
