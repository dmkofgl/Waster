package waster.domain.service;

import waster.domain.entity.PlanReport;

import java.util.List;
import java.util.Optional;

public interface PlanReportService {
    PlanReport save(PlanReport planReport);

    Optional<PlanReport> findById(Long id);

    List<PlanReport> findAll();
}
