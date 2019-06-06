package waster.domain.repository.abstracts;

import org.springframework.data.jpa.repository.JpaRepository;
import waster.domain.entity.PlanReport;

public interface PlanReportRepository extends JpaRepository<PlanReport,Long> {
}
