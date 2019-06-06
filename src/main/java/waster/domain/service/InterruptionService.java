package waster.domain.service;

import waster.domain.entity.calendar.Interruption;
import waster.domain.entity.calendar.Operation;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface InterruptionService {
    List<Interruption> getAllOverlapInterruptions(Operation operation, Date startDate);

    Optional<Interruption> getLastOverlapInterruption(Operation operation, Date startDate);

    Optional<Interruption> getFirstOverlapInterruption(Operation operation, Date startDate);
}
