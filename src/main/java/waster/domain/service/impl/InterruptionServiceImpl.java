package waster.domain.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import waster.domain.entity.calendar.Interruption;
import waster.domain.entity.calendar.Operation;
import waster.domain.repository.abstracts.InterruptionRepository;
import waster.domain.service.InterruptionService;
import waster.utuls.DateUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class InterruptionServiceImpl implements InterruptionService {

    @Autowired
    private InterruptionRepository interruptionRepository;

    @Override
    public List<Interruption> getAllOverlapInteraptions(Operation operation, Date startDate) {
        List<Interruption> result = new ArrayList<>();
        List<Interruption> interruptions = interruptionRepository.findAll();
        interruptions.sort(Comparator.comparingLong(x -> x.getEnd().getTime()));
        for (Interruption interruption : interruptions) {
            if (isOverlapWithInterruption(operation, interruption, startDate)) {
                result.add(interruption);
            }
        }
        return result;
    }

    private boolean isOverlapWithInterruption(Operation operation, Interruption interruption, Date date) {
        Long startA = interruption.getStart().getTime();
        Long endA = interruption.getEnd().getTime();
        Long startB = operation.getInitialStartDate() + date.getTime();
        Long endB = operation.getEndTime() + date.getTime();
        return DateUtils.isOverlap(startA, endA, startB, endB);
    }
}
