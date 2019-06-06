package waster.domain.repository.abstracts;

import org.springframework.data.jpa.repository.JpaRepository;
import waster.domain.entity.KPV;

public interface KPVRepository extends JpaRepository<KPV,Long> {
}
