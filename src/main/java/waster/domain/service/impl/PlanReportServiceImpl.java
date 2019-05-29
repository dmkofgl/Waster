package waster.domain.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import waster.domain.entity.PlanReport;
import waster.domain.repository.abstracts.PlanReportRepository;
import waster.domain.service.PlanReportService;

import java.util.List;
import java.util.Optional;

@Service
public class PlanReportServiceImpl implements PlanReportService {
    @Autowired
    private PlanReportRepository planReportRepository;

    @Override
    public PlanReport save(PlanReport planReport) {
        return planReportRepository.save(planReport);
    }

    @Override
    public Optional<PlanReport> findById(Long id) {
        return planReportRepository.findById(id);
    }

    @Override
    public List<PlanReport> findAll() {
        return planReportRepository.findAll();
    }
}
